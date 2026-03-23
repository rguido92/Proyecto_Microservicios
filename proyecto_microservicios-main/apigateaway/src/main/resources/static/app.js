const sessionKeys = {
  user: "microservices.session.user",
  password: "microservices.session.password",
  role: "microservices.session.role",
  userId: "microservices.session.userId"
};

function getSession() {
  return {
    nombre: localStorage.getItem(sessionKeys.user) || "",
    contrasena: localStorage.getItem(sessionKeys.password) || "",
    rol: localStorage.getItem(sessionKeys.role) || "",
    usuario_id: Number(localStorage.getItem(sessionKeys.userId) || 0)
  };
}

function setSession(usuario) {
  localStorage.setItem(sessionKeys.user, usuario.nombre || "");
  localStorage.setItem(sessionKeys.password, usuario.contrasena || "");
  localStorage.setItem(sessionKeys.role, (usuario.rol || "USER").toUpperCase());
  localStorage.setItem(sessionKeys.userId, String(usuario.usuario_id || 0));
}

function clearSession() {
  Object.values(sessionKeys).forEach((key) => localStorage.removeItem(key));
}

function isAdmin() {
  return getSession().rol === "ADMIN";
}

function setFeedback(id, message, type = "success") {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = message || "";
  el.className = `feedback ${type}`;
}

function safeJson(text) {
  if (!text) return null;
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

async function request(url, options = {}) {
  const response = await fetch(url, options);
  const text = await response.text();
  const payload = safeJson(text);
  if (!response.ok) {
    throw new Error(typeof payload === "string" ? payload : JSON.stringify(payload));
  }
  return payload;
}

async function graphql(query, variables = {}) {
  const payload = await request("/comentarios", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ query, variables })
  });

  if (payload.errors && payload.errors.length) {
    throw new Error(payload.errors.map((error) => error.message).join(" | "));
  }

  return payload.data;
}

function formToObject(form) {
  return Object.fromEntries(new FormData(form).entries());
}

function formatCell(value) {
  if (value === null || value === undefined) return "-";
  if (typeof value === "object") return `<pre>${JSON.stringify(value, null, 2)}</pre>`;
  return String(value);
}

function renderTable(containerId, rows) {
  const container = document.getElementById(containerId);
  if (!container) return;
  if (!rows || rows.length === 0) {
    container.innerHTML = "<p>No hay datos para mostrar.</p>";
    return;
  }

  const columns = Array.from(
    rows.reduce((set, row) => {
      Object.keys(row).forEach((key) => set.add(key));
      return set;
    }, new Set())
  );

  const head = columns.map((column) => `<th>${column}</th>`).join("");
  const body = rows
    .map((row) => `<tr>${columns.map((column) => `<td>${formatCell(row[column])}</td>`).join("")}</tr>`)
    .join("");

  container.innerHTML = `<table><thead><tr>${head}</tr></thead><tbody>${body}</tbody></table>`;
}

function updateCounter(id, value) {
  const el = document.getElementById(id);
  if (el) el.textContent = String(value);
}

function getAuthPayload() {
  const session = getSession();
  return {
    nombre: session.nombre,
    contrasena: session.contrasena
  };
}

function getPasswordWithEnye() {
  return { ["contrase" + "\u00f1" + "a"]: getSession().contrasena };
}

async function login(nombre, contrasena) {
  return request("/usuarios/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre, contrasena })
  });
}

function showAuthenticatedWorkspace() {
  const session = getSession();
  const authScreen = document.getElementById("auth-screen");
  const workspace = document.getElementById("workspace");
  const adminDashboard = document.getElementById("admin-dashboard");
  const userDashboard = document.getElementById("user-dashboard");

  authScreen.classList.add("hidden");
  authScreen.style.display = "none";
  workspace.classList.remove("hidden");
  workspace.style.display = "grid";
  document.getElementById("identity-name").textContent = session.nombre;
  document.getElementById("identity-role").textContent = session.rol || "USER";

  if (isAdmin()) {
    document.getElementById("workspace-title").textContent = "Panel de administración";
    adminDashboard.classList.remove("hidden");
    adminDashboard.style.display = "block";
    userDashboard.classList.add("hidden");
    userDashboard.style.display = "none";
  } else {
    document.getElementById("workspace-title").textContent = "Mi espacio de usuario";
    userDashboard.classList.remove("hidden");
    userDashboard.style.display = "block";
    adminDashboard.classList.add("hidden");
    adminDashboard.style.display = "none";
  }
}

function showLoginScreen() {
  const authScreen = document.getElementById("auth-screen");
  const workspace = document.getElementById("workspace");
  const adminDashboard = document.getElementById("admin-dashboard");
  const userDashboard = document.getElementById("user-dashboard");

  workspace.classList.add("hidden");
  workspace.style.display = "none";
  authScreen.classList.remove("hidden");
  authScreen.style.display = "grid";
  adminDashboard.classList.add("hidden");
  adminDashboard.style.display = "none";
  userDashboard.classList.add("hidden");
  userDashboard.style.display = "none";
}

async function loadUsers() {
  const users = await request("/usuarios");
  updateCounter("admin-count-users", users.length);
  renderTable("admin-users-table", users);
}

async function loadHospitality(prefix) {
  const [hotels, rooms] = await Promise.all([
    request("/reservas/hotel"),
    request("/reservas/habitacion")
  ]);

  if (prefix === "admin") {
    updateCounter("admin-count-hotels", hotels.length);
    updateCounter("admin-count-rooms", rooms.length);
    renderTable("admin-hotels-table", hotels);
    renderTable("admin-rooms-table", rooms);
    return;
  }

  updateCounter("user-count-hotels", hotels.length);
  updateCounter("user-count-rooms", rooms.length);
  renderTable("user-hotels-table", hotels);
  renderTable("user-rooms-table", rooms);
}

async function loadReservationsByUser(containerId = "user-reservations-table", counterId = "user-count-reservations") {
  const reservas = await request("/reservas/listar-usuario", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(getAuthPayload())
  });
  updateCounter(counterId, reservas.length);
  renderTable(containerId, reservas);
  return reservas;
}

async function loadReservationsByState(estado, containerId = "admin-reservations-table", counterId = "admin-count-reservations") {
  const reservas = await request(`/reservas/listar-estado?estado=${encodeURIComponent(estado)}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(getAuthPayload())
  });
  updateCounter(counterId, reservas.length);
  renderTable(containerId, reservas);
  return reservas;
}

async function loadCommentsByUser(containerId = "user-comments-table", counterId = "user-count-comments") {
  const data = await graphql(
    `query($input: UserPassDTOInput) {
      listarComentariosUsuario(userPassDTO: $input) {
        id
        nombreHotel
        reservaId
        puntuacion
        comentario
      }
    }`,
    { input: getAuthPayload() }
  );

  const comments = data.listarComentariosUsuario || [];
  updateCounter(counterId, comments.length);
  renderTable(containerId, comments);
  return comments;
}

async function loadCommentsByHotel(nombreHotel) {
  const data = await graphql(
    `query($input: ListarComentariosHotelInput) {
      listarComentariosHotel(listarComentariosHotelDTO: $input) {
        id
        nombreHotel
        reservaId
        puntuacion
        comentario
      }
    }`,
    { input: { ...getAuthPayload(), nombreHotel } }
  );

  const comments = data.listarComentariosHotel || [];
  renderTable("admin-comments-table", comments);
  return comments;
}

async function scoreHotel(nombreHotel) {
  const data = await graphql(
    `query($input: ObtenerHotelDTO) {
      puntuacionMediaHotel(obtenerHotelDTO: $input)
    }`,
    { input: { nombreHotel, ...getAuthPayload() } }
  );
  return data.puntuacionMediaHotel;
}

async function scoreUser() {
  const data = await graphql(
    `query($input: UserPassDTOInput) {
      puntuacionesMediasUsuario(userPassDTO: $input)
    }`,
    { input: getAuthPayload() }
  );
  return data.puntuacionesMediasUsuario;
}

async function refreshCurrentDashboard() {
  if (isAdmin()) {
    await Promise.allSettled([loadUsers(), loadHospitality("admin"), loadReservationsByState("Pendiente")]);
    return;
  }

  await Promise.allSettled([loadHospitality("user"), loadReservationsByUser(), loadCommentsByUser()]);
}

function attachSubmit(formId, feedbackId, handler) {
  const form = document.getElementById(formId);
  if (!form) return;

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    try {
      const message = await handler(event.currentTarget);
      setFeedback(feedbackId, message, "success");
    } catch (error) {
      setFeedback(feedbackId, error.message, "error");
    }
  });
}

function bindLogin() {
  document.getElementById("login-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const values = formToObject(event.currentTarget);
    let usuario;

    try {
      usuario = await login(values.nombre, values.contrasena);
    } catch (error) {
      clearSession();
      showLoginScreen();
      setFeedback("auth-feedback", "No se ha podido iniciar sesión. Revisa usuario, contraseña y rol asignado.", "error");
      return;
    }

    setSession(usuario);
    showAuthenticatedWorkspace();
    setFeedback("auth-feedback", "");
    event.currentTarget.reset();

    try {
      await refreshCurrentDashboard();
    } catch (error) {
      setFeedback("auth-feedback", `Sesión iniciada, pero el panel no se ha cargado completo: ${error.message}`, "error");
    }
  });

  document.getElementById("logout-button").addEventListener("click", () => {
    clearSession();
    showLoginScreen();
    setFeedback("auth-feedback", "Sesión cerrada.", "success");
  });
}

function bindButtons() {
  document.querySelector('[data-action="admin-load-users"]').addEventListener("click", () => loadUsers().catch((error) => setFeedback("admin-users-feedback", error.message, "error")));
  document.querySelector('[data-action="admin-load-hospitality"]').addEventListener("click", () => loadHospitality("admin").catch((error) => setFeedback("admin-hospitality-feedback", error.message, "error")));
  document.querySelector('[data-action="admin-load-reservations"]').addEventListener("click", () => loadReservationsByState("Pendiente").catch((error) => setFeedback("admin-reservations-feedback", error.message, "error")));
  document.querySelector('[data-action="admin-load-comments"]').addEventListener("click", () => setFeedback("admin-comments-feedback", "Introduce un hotel y pulsa consultar para cargar comentarios.", "success"));
  document.querySelector('[data-action="user-load-hospitality"]').addEventListener("click", () => loadHospitality("user").catch((error) => setFeedback("user-hospitality-feedback", error.message, "error")));
  document.querySelector('[data-action="user-load-reservations"]').addEventListener("click", () => loadReservationsByUser().catch((error) => setFeedback("user-reservations-feedback", error.message, "error")));
  document.querySelector('[data-action="user-load-comments"]').addEventListener("click", () => loadCommentsByUser().catch((error) => setFeedback("user-comments-feedback", error.message, "error")));
}

function bindAdminForms() {
  attachSubmit("admin-create-user-form", "admin-users-feedback", async (form) => {
    const message = await request("/usuarios/registrar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formToObject(form))
    });
    await loadUsers();
    form.reset();
    return message;
  });

  attachSubmit("admin-update-user-form", "admin-users-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/usuarios/registrar", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: Number(values.id),
        nombre: values.nombre,
        correo_electronico: values.correo_electronico,
        direccion: values.direccion,
        contrasena: values.contrasena,
        rol: values.rol
      })
    });
    await loadUsers();
    return message;
  });

  attachSubmit("admin-delete-user-form", "admin-users-feedback", async (form) => {
    const message = await request("/usuarios/", {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formToObject(form))
    });
    await loadUsers();
    form.reset();
    return message;
  });

  attachSubmit("admin-create-hotel-form", "admin-hospitality-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas/hotel", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ...values, ...getAuthPayload(), usuario: getSession().nombre })
    });
    await loadHospitality("admin");
    form.reset();
    return message;
  });

  attachSubmit("admin-update-hotel-form", "admin-hospitality-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas/hotel", {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        hotelId: Number(values.hotelId),
        nombre: values.nombre,
        direccion: values.direccion,
        usuario: getSession().nombre,
        contrasena: getSession().contrasena
      })
    });
    await loadHospitality("admin");
    return message;
  });

  attachSubmit("admin-delete-hotel-form", "admin-hospitality-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request(`/reservas/hotel/${values.id}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nombre: getSession().nombre, ...getPasswordWithEnye() })
    });
    await loadHospitality("admin");
    form.reset();
    return message;
  });

  attachSubmit("admin-create-room-form", "admin-hospitality-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas/habitacion", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: getSession().nombre,
        ...getPasswordWithEnye(),
        hotelId: Number(values.hotelId),
        numero_habitacion: Number(values.numero_habitacion),
        tipo: values.tipo,
        precio: Number(values.precio),
        disponible: true
      })
    });
    await loadHospitality("admin");
    form.reset();
    return message;
  });

  attachSubmit("admin-update-room-form", "admin-hospitality-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas/habitacion", {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: getSession().nombre,
        ...getPasswordWithEnye(),
        id: Number(values.id),
        hotelId: Number(values.hotelId),
        numero_habitacion: Number(values.numero_habitacion),
        tipo: values.tipo,
        precio: Number(values.precio),
        disponible: values.disponible === "true"
      })
    });
    await loadHospitality("admin");
    return message;
  });

  attachSubmit("admin-delete-room-form", "admin-hospitality-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request(`/reservas/habitacion/${values.id}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nombre: getSession().nombre, ...getPasswordWithEnye() })
    });
    await loadHospitality("admin");
    form.reset();
    return message;
  });

  attachSubmit("admin-create-reservation-form", "admin-reservations-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...getAuthPayload(),
        habitacion_id: Number(values.habitacion_id),
        fecha_inicio: values.fecha_inicio,
        fecha_fin: values.fecha_fin,
        estado: values.estado
      })
    });
    await loadReservationsByState("Pendiente");
    form.reset();
    return message;
  });

  attachSubmit("admin-update-reservation-form", "admin-reservations-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas", {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...getAuthPayload(),
        reserva_id: Number(values.reserva_id),
        estado: values.estado
      })
    });
    await loadReservationsByState(values.estado);
    return message;
  });

  attachSubmit("admin-list-reservations-state-form", "admin-reservations-feedback", async (form) => {
    const values = formToObject(form);
    await loadReservationsByState(values.estado);
    return `Reservas con estado ${values.estado} cargadas.`;
  });

  attachSubmit("admin-check-reservation-form", "admin-reservations-feedback", async (form) => {
    const values = formToObject(form);
    const found = await request(`/reservas/check?idUsuario=${values.idUsuario}&idReserva=${values.idReserva}&idHotel=${values.idHotel}`);
    return found ? "La reserva existe." : "La reserva no existe.";
  });

  attachSubmit("admin-list-comments-hotel-form", "admin-comments-feedback", async (form) => {
    const values = formToObject(form);
    const comments = await loadCommentsByHotel(values.nombreHotel);
    return `Se han cargado ${comments.length} comentarios del hotel ${values.nombreHotel}.`;
  });

  attachSubmit("admin-score-hotel-form", "admin-comments-feedback", async (form) => {
    const values = formToObject(form);
    const score = await scoreHotel(values.nombreHotel);
    return `Puntuación media del hotel: ${score}`;
  });
}

function bindUserForms() {
  attachSubmit("user-create-reservation-form", "user-reservations-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...getAuthPayload(),
        habitacion_id: Number(values.habitacion_id),
        fecha_inicio: values.fecha_inicio,
        fecha_fin: values.fecha_fin,
        estado: values.estado
      })
    });
    await loadReservationsByUser();
    form.reset();
    return message;
  });

  attachSubmit("user-update-reservation-form", "user-reservations-feedback", async (form) => {
    const values = formToObject(form);
    const message = await request("/reservas", {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...getAuthPayload(),
        reserva_id: Number(values.reserva_id),
        estado: values.estado
      })
    });
    await loadReservationsByUser();
    return message;
  });

  attachSubmit("user-list-reservations-form", "user-reservations-feedback", async () => {
    await loadReservationsByUser();
    return "Tus reservas se han cargado correctamente.";
  });

  attachSubmit("user-create-comment-form", "user-comments-feedback", async (form) => {
    const values = formToObject(form);
    const session = getSession();
    const data = await graphql(
      `mutation($input: CrearComentarioInput) {
        crearComentario(comentarioDTO: $input) {
          nombreUsuario
          nombreHotel
          reservaId
          puntuacion
          comentario
        }
      }`,
      {
        input: {
          nombreHotel: values.nombreHotel,
          nombreUsuario: session.nombre,
          contrasena: session.contrasena,
          reservaId: Number(values.reservaId),
          comentario: values.comentario,
          puntuacion: Number(values.puntuacion)
        }
      }
    );
    renderTable("user-comments-table", [data.crearComentario]);
    await loadCommentsByUser();
    form.reset();
    return "Comentario creado correctamente.";
  });

  attachSubmit("user-delete-comment-form", "user-comments-feedback", async (form) => {
    const values = formToObject(form);
    const data = await graphql(
      `mutation($input: EliminarComentarioDTO) {
        eliminarComentarioDTO(eliminarComentarioDTO: $input)
      }`,
      { input: { ...getAuthPayload(), id: values.id } }
    );
    await loadCommentsByUser();
    form.reset();
    return data.eliminarComentarioDTO;
  });

  attachSubmit("user-list-comments-form", "user-comments-feedback", async () => {
    await loadCommentsByUser();
    return "Tus comentarios se han cargado correctamente.";
  });

  attachSubmit("user-score-form", "user-comments-feedback", async () => {
    const score = await scoreUser();
    return `Tu puntuación media es ${score}`;
  });
}

async function restoreSession() {
  const session = getSession();
  if (!session.nombre || !session.contrasena) {
    showLoginScreen();
    return;
  }

  try {
    const usuario = await login(session.nombre, session.contrasena);
    setSession(usuario);
    showAuthenticatedWorkspace();
    await refreshCurrentDashboard();
  } catch {
    clearSession();
    showLoginScreen();
  }
}

function bootstrap() {
  bindLogin();
  bindButtons();
  bindAdminForms();
  bindUserForms();
  restoreSession();
}

bootstrap();
