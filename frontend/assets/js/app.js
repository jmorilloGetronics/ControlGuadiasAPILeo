const API_BASE = "http://localhost:4000/api";
const AUTH_TOKEN_KEY = "guardias_token";

const state = {
  partes: [],
  parteActual: null,
  franjas: [],
  indiceActual: 0,
  centro: null,
  token: null,
  usuarioAutenticado: null
};

const tiempos = {
  inicioApp: performance.now(),
  health: null,
  config: null,
  partes: null,
  parte: null,
  totalCarga: null
};

const rowsEl = document.getElementById("rows");
const cabeceraParteEl = document.getElementById("cabeceraParte");
const fechaEtiquetaEl = document.getElementById("fechaEtiqueta");
const usuarioConectadoEl = document.getElementById("usuarioConectado");
const btnManana = document.getElementById("btnManana");
const btnOtroDia = document.getElementById("btnOtroDia");
const btnComunicarAusencia = document.getElementById("btnComunicarAusencia");
const metricasEl = document.getElementById("metricas-tiempo");
const appShellEl = document.getElementById("appShell");
const loginViewEl = document.getElementById("loginView");
const loginFormEl = document.getElementById("loginForm");
const loginUsuarioEl = document.getElementById("loginUsuario");
const loginPasswordEl = document.getElementById("loginPassword");
const loginMensajeEl = document.getElementById("loginMensaje");
const menuTriggerEl = document.getElementById("menuTrigger");
const menuDropdownEl = document.getElementById("menuDropdown");
const logoutBtnEl = document.getElementById("logoutBtn");

function cerrarMenuDropdown() {
  menuDropdownEl.classList.add("hidden");
  menuTriggerEl.setAttribute("aria-expanded", "false");
}

function alternarMenuDropdown() {
  const abierto = !menuDropdownEl.classList.contains("hidden");

  if (abierto) {
    cerrarMenuDropdown();
    return;
  }

  menuDropdownEl.classList.remove("hidden");
  menuTriggerEl.setAttribute("aria-expanded", "true");
}

function mostrarLogin(mensaje = "") {
  appShellEl.classList.add("hidden");
  loginViewEl.classList.remove("hidden");
  loginMensajeEl.textContent = mensaje;
  cerrarMenuDropdown();
}

function mostrarApp() {
  loginViewEl.classList.add("hidden");
  appShellEl.classList.remove("hidden");
  cerrarMenuDropdown();
  const hoy = new Date();
  const dd = String(hoy.getDate()).padStart(2, "0");
  const mm = String(hoy.getMonth() + 1).padStart(2, "0");
  const yyyy = hoy.getFullYear();
  fechaEtiquetaEl.textContent = `${dd}-${mm}-${yyyy}`;
}

function cabecerasAuth(extra = {}) {
  if (!state.token) {
    return extra;
  }

  return {
    ...extra,
    Authorization: `Bearer ${state.token}`
  };
}

function cerrarSesion() {
  state.token = null;
  state.usuarioAutenticado = null;
  localStorage.removeItem(AUTH_TOKEN_KEY);
}

function numeroHora(etiqueta) {
  const match = etiqueta.match(/(\d+)/);
  return match ? Number(match[1]) : null;
}

function etiquetaHoraChip(franja) {
  const numero = numeroHora(franja.etiqueta);
  return numero ? `Hora: ${numero}` : franja.etiqueta;
}

function etiquetaHoraTag(franja) {
  const numero = numeroHora(franja.etiqueta);
  return numero ? `${numero}a hora` : franja.etiqueta.toLowerCase();
}

function formatoFecha(fechaISO) {
  const [yyyy, mm, dd] = fechaISO.split("-");
  return `${dd}-${mm}-${yyyy}`;
}

async function leerJSON(url, opciones = {}) {
  const inicio = performance.now();
  const response = await fetch(url, opciones);
  const tiempoFetch = performance.now() - inicio;
  
  if (!response.ok) {
    const error = new Error(`Error ${response.status} al consultar ${url}`);
    error.status = response.status;
    throw error;
  }
  
  const data = await response.json();
  console.log(`📡 Fetch ${url}: ${tiempoFetch.toFixed(2)}ms`);
  
  return { data, tiempo: tiempoFetch };
}

async function postJSON(url, body) {
  return leerJSON(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(body)
  });
}

async function postJSONAuth(url, body) {
  return leerJSON(url, {
    method: "POST",
    headers: cabecerasAuth({
      "Content-Type": "application/json"
    }),
    body: JSON.stringify(body)
  });
}

async function putJSONAuth(url, body) {
  return leerJSON(url, {
    method: "PUT",
    headers: cabecerasAuth({
      "Content-Type": "application/json"
    }),
    body: JSON.stringify(body)
  });
}

async function deleteJSONAuth(url) {
  return leerJSON(url, {
    method: "DELETE",
    headers: cabecerasAuth()
  });
}

function obtenerFranja(franjaId) {
  return state.franjas.find((franja) => franja.id === franjaId);
}

function escapeHtml(valor) {
  return String(valor ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}

function obtenerAusenciaEnParte(ausenciaId) {
  if (!state.parteActual) {
    return null;
  }

  for (const bloque of state.parteActual.bloques) {
    const ausencia = (bloque.ausenciasTareas || []).find((item) => item.id === ausenciaId);
    if (ausencia) {
      return { bloque, ausencia };
    }
  }

  return null;
}

function pedirCampoObligatorio(etiqueta, valorInicial = "") {
  const valor = window.prompt(etiqueta, valorInicial);
  if (valor === null) {
    return null;
  }

  const limpio = valor.trim();
  if (!limpio) {
    window.alert(`El campo "${etiqueta}" es obligatorio.`);
    return null;
  }

  return limpio;
}

function solicitarDatosAusencia(base = {}, franjaSugerida = "") {
  const franjasValidas = state.franjas
    .filter((franja) => franja.id !== "recreo")
    .map((franja) => franja.id);

  const franjaId = pedirCampoObligatorio(
    `Franja (${franjasValidas.join(", ")})`,
    base.franjaId || franjaSugerida || franjasValidas[0] || "h1"
  );
  if (!franjaId) {
    return null;
  }

  if (!franjasValidas.includes(franjaId)) {
    window.alert("Franja no valida.");
    return null;
  }

  const profesorAusente = pedirCampoObligatorio("Profesor ausente", base.profesorAusente || "");
  if (!profesorAusente) {
    return null;
  }

  const grupo = pedirCampoObligatorio("Grupo", base.grupo || "");
  if (!grupo) {
    return null;
  }

  const aula = pedirCampoObligatorio("Aula", base.aula || "");
  if (!aula) {
    return null;
  }

  const asignatura = pedirCampoObligatorio("Asignatura", base.asignatura || "");
  if (!asignatura) {
    return null;
  }

  const tarea = pedirCampoObligatorio("Tarea", base.tarea || "");
  if (!tarea) {
    return null;
  }

  return {
    franjaId,
    profesorAusente,
    grupo,
    aula,
    asignatura,
    tarea
  };
}

function gestionarErrorCrud(error, mensaje) {
  if (error.status === 401) {
    cerrarSesion();
    mostrarLogin("Sesion no valida. Vuelve a iniciar sesion.");
    return;
  }

  const detalle = error.status ? ` (HTTP ${error.status})` : "";
  window.alert(`${mensaje}${detalle}`);
  console.error(error);
}

async function crearAusencia() {
  if (!state.parteActual) {
    return;
  }

  const datos = solicitarDatosAusencia({}, state.parteActual.bloques[0]?.franjaId || "h1");
  if (!datos) {
    return;
  }

  try {
    await postJSONAuth(`${API_BASE}/ausencias`, {
      fecha: state.parteActual.fecha,
      ...datos
    });
    await cargarParte(state.indiceActual);
  } catch (error) {
    gestionarErrorCrud(error, "No se pudo crear la ausencia");
  }
}

async function editarAusencia(ausenciaId) {
  if (!state.parteActual) {
    return;
  }

  const encontrada = obtenerAusenciaEnParte(ausenciaId);
  if (!encontrada) {
    window.alert("No se encontro la ausencia seleccionada.");
    return;
  }

  const datos = solicitarDatosAusencia(
    {
      ...encontrada.ausencia,
      franjaId: encontrada.bloque.franjaId
    },
    encontrada.bloque.franjaId
  );
  if (!datos) {
    return;
  }

  try {
    await putJSONAuth(`${API_BASE}/ausencias/${encodeURIComponent(ausenciaId)}`, {
      fecha: state.parteActual.fecha,
      ...datos
    });
    await cargarParte(state.indiceActual);
  } catch (error) {
    gestionarErrorCrud(error, "No se pudo editar la ausencia");
  }
}

async function eliminarAusencia(ausenciaId) {
  if (!state.parteActual) {
    return;
  }

  const confirmacion = window.confirm("Quieres eliminar esta ausencia?");
  if (!confirmacion) {
    return;
  }

  try {
    await deleteJSONAuth(
      `${API_BASE}/ausencias/${encodeURIComponent(ausenciaId)}?fecha=${encodeURIComponent(state.parteActual.fecha)}`
    );
    await cargarParte(state.indiceActual);
  } catch (error) {
    gestionarErrorCrud(error, "No se pudo eliminar la ausencia");
  }
}

function pintarParte() {
  if (!state.parteActual) {
    rowsEl.innerHTML = "<p>No hay datos de guardias para mostrar.</p>";
    return;
  }

  cabeceraParteEl.textContent = `Parte de: ${formatoFecha(state.parteActual.fecha)} (${state.parteActual.diaSemana}). Ver parte de:`;
  usuarioConectadoEl.textContent = `Usuario conectado: ${state.usuarioAutenticado}`;

  const htmlFilas = state.parteActual.bloques
    .map((bloque) => {
      const franja = obtenerFranja(bloque.franjaId);
      const franjaTxt = franja
        ? `${franja.etiqueta} (${franja.inicio} - ${franja.fin})`
        : bloque.franjaId;

      const guardias = bloque.profesoresGuardia
        .map(
          (guardia) => `
            <li>
              <span class="teacher-name">${guardia.nombre}</span>
              <span class="teacher-location">(${guardia.ubicacion})</span>
            </li>
          `
        )
        .join("");

      const incidencias = (bloque.ausenciasTareas || [])
        .map(
          (incidencia) => {
            const id = escapeHtml(incidencia.id || "");
            const profesor = escapeHtml(incidencia.profesorAusente);
            const grupo = escapeHtml(incidencia.grupo);
            const aula = escapeHtml(incidencia.aula);
            const asignatura = escapeHtml(incidencia.asignatura);
            const tarea = escapeHtml(incidencia.tarea);

            return `
            <li class="incidencia-item">
              <strong>${profesor}</strong>: Grupo ${grupo} - Aula ${aula}
              <br />
              ${asignatura}: ${tarea}
              <div class="incidencia-actions">
                <button class="mini-btn btn-editar-ausencia" data-ausencia-id="${id}" type="button">Editar</button>
                <button class="mini-btn mini-btn-danger btn-eliminar-ausencia" data-ausencia-id="${id}" type="button">Eliminar</button>
              </div>
            </li>
          `;
          }
        )
        .join("");

      return `
        <article class="row">
          <div class="col col-left">
            <span class="hour-chip">${franja ? etiquetaHoraChip(franja) : "Franja"}</span>
            <div class="meta-line">${franjaTxt}</div>
            <ol class="list">${guardias}</ol>
          </div>
          <div class="col">
            <div class="tag-line">
              <span>Ausencias y tareas para hoy</span>
              <span class="tag tag-blue">${state.parteActual.diaSemana}</span>
              <span class="tag tag-yellow">${franja ? etiquetaHoraTag(franja) : "franja"}</span>
            </div>
            <ol class="list">${incidencias || "<li>Sin incidencias</li>"}</ol>
          </div>
        </article>
      `;
    })
    .join("");

  const recreo = state.franjas.find((franja) => franja.id === "recreo");
  const recreoHtml = recreo
    ? `<div class="recreo-card"><strong>${recreo.etiqueta}</strong>: ${recreo.inicio} - ${recreo.fin} (30 min)</div>`
    : "";

  rowsEl.innerHTML = htmlFilas + recreoHtml;
}

async function cargarParte(indice) {
  if (indice < 0 || indice >= state.partes.length) {
    return;
  }

  state.indiceActual = indice;
  const fecha = state.partes[indice].fecha;

  const { data, tiempo } = await leerJSON(`${API_BASE}/parte/${fecha}`, {
    headers: cabecerasAuth()
  });
  tiempos.parte = tiempo;
  
  state.centro = data.centro;
  state.franjas = data.franjas;
  state.parteActual = data.parte;
  pintarParte();
}

async function validarSesion() {
  const { data } = await leerJSON(`${API_BASE}/auth/me`, {
    headers: cabecerasAuth()
  });

  state.usuarioAutenticado = data.usuario;
}

async function init() {
  try {
    tiempos.inicioApp = performance.now();

    // Health check (opcional, para validar servidor)
    const healthResp = await leerJSON(`${API_BASE}/health`);
    tiempos.health = healthResp.tiempo;

    // Obtener lista de partes
    const partesResp = await leerJSON(`${API_BASE}/partes`, {
      headers: cabecerasAuth()
    });
    tiempos.partes = partesResp.tiempo;
    
    const dataPartes = partesResp.data;
    state.partes = dataPartes.partes;

    if (!state.partes.length) {
      rowsEl.innerHTML = "<p>No hay partes disponibles.</p>";
      return;
    }

    await cargarParte(0);
    
    tiempos.totalCarga = performance.now() - tiempos.inicioApp;
    
    // Log de tiempos
    console.log("%c=== MÉTRICAS DE RENDIMIENTO ===", "color: #2ecc71; font-weight: bold; font-size: 14px");
    console.log(`⏱️  Health Check: ${tiempos.health.toFixed(2)}ms`);
    console.log(`⏱️  Obtener Partes: ${tiempos.partes.toFixed(2)}ms`);
    console.log(`⏱️  Cargar Parte Inicial: ${tiempos.parte.toFixed(2)}ms`);
    console.log(`%c⏱️  TIEMPO TOTAL DE CARGA: ${tiempos.totalCarga.toFixed(2)}ms`, "color: #e74c3c; font-weight: bold; font-size: 12px");
    
    // Actualizar UI
    if (metricasEl) {
      metricasEl.textContent = `Tiempo de carga total: ${tiempos.totalCarga.toFixed(2)}ms`;
    }
    
  } catch (error) {
    if (error.status === 401) {
      cerrarSesion();
      mostrarLogin("Sesion no valida. Vuelve a iniciar sesion.");
      return;
    }

    rowsEl.innerHTML = `
      <p>
        No se pudieron cargar los datos del backend.
        Revisa que la API este ejecutandose en ${API_BASE}.
      </p>
    `;
    console.error("❌ Error en init():", error);
  }
}

loginFormEl.addEventListener("submit", async (event) => {
  event.preventDefault();

  const usuario = loginUsuarioEl.value.trim();
  const password = loginPasswordEl.value;

  if (!usuario || !password) {
    loginMensajeEl.textContent = "Completa usuario y contrasena.";
    return;
  }

  loginMensajeEl.textContent = "Validando credenciales...";

  try {
    const { data } = await postJSON(`${API_BASE}/auth/login`, {
      usuario,
      password
    });

    state.token = data.token;
    state.usuarioAutenticado = data.usuario;
    localStorage.setItem(AUTH_TOKEN_KEY, data.token);
    loginPasswordEl.value = "";
    mostrarApp();
    await init();
  } catch (error) {
    loginMensajeEl.textContent = "Credenciales invalidas. Usa usuario/usuario.";
  }
});

menuTriggerEl.addEventListener("click", (event) => {
  event.stopPropagation();
  alternarMenuDropdown();
});

logoutBtnEl.addEventListener("click", () => {
  cerrarSesion();
  state.partes = [];
  state.parteActual = null;
  rowsEl.innerHTML = "";
  loginFormEl.reset();
  mostrarLogin("Sesion cerrada.");
  loginUsuarioEl.focus();
});

btnComunicarAusencia.addEventListener("click", () => {
  crearAusencia();
});

rowsEl.addEventListener("click", (event) => {
  const botonEditar = event.target.closest(".btn-editar-ausencia");
  if (botonEditar) {
    const ausenciaId = botonEditar.getAttribute("data-ausencia-id");
    if (ausenciaId) {
      editarAusencia(ausenciaId);
    }
    return;
  }

  const botonEliminar = event.target.closest(".btn-eliminar-ausencia");
  if (botonEliminar) {
    const ausenciaId = botonEliminar.getAttribute("data-ausencia-id");
    if (ausenciaId) {
      eliminarAusencia(ausenciaId);
    }
  }
});

document.addEventListener("click", (event) => {
  if (menuDropdownEl.classList.contains("hidden")) {
    return;
  }

  const clickDentroDelMenu =
    menuDropdownEl.contains(event.target) || menuTriggerEl.contains(event.target);

  if (!clickDentroDelMenu) {
    cerrarMenuDropdown();
  }
});

document.addEventListener("keydown", (event) => {
  if (event.key === "Escape") {
    cerrarMenuDropdown();
  }
});

btnManana.addEventListener("click", () => {
  const siguiente = Math.min(state.indiceActual + 1, state.partes.length - 1);
  cargarParte(siguiente);
});

btnOtroDia.addEventListener("click", () => {
  const entrada = window.prompt("Introduce fecha (YYYY-MM-DD)", state.parteActual?.fecha || "");

  if (!entrada) {
    return;
  }

  const indice = state.partes.findIndex((parte) => parte.fecha === entrada.trim());

  if (indice === -1) {
    window.alert("No hay parte para esa fecha.");
    return;
  }

  cargarParte(indice);
});

async function bootstrap() {
  const tokenGuardado = localStorage.getItem(AUTH_TOKEN_KEY);

  if (!tokenGuardado) {
    mostrarLogin("Acceso requerido. Usa usuario/usuario.");
    return;
  }

  state.token = tokenGuardado;

  try {
    await validarSesion();
    mostrarApp();
    await init();
  } catch (error) {
    cerrarSesion();
    mostrarLogin("Acceso requerido. Usa usuario/usuario.");
  }
}

bootstrap();
