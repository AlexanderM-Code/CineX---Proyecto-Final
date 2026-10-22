const TMDB_API_KEY = 'ab3a96eb02c4788f094f1803fa62a7b3';
const TMDB_BASE = 'https://api.themoviedb.org/3';
const TMDB_IMAGE = 'https://image.tmdb.org/t/p/original';
// En local apunta al backend en el puerto 8080; publicado, a la API en Render.
const CINEX_BASE = ['localhost', '127.0.0.1', ''].includes(window.location.hostname)
  ? 'http://localhost:8080'
  : 'https://cinex-api-lt2q.onrender.com';

document.addEventListener('DOMContentLoaded', () => {
  const yearEls = document.querySelectorAll('#year, #year-detail, #year-about');
  yearEls.forEach(el => el.textContent = new Date().getFullYear());

  const path = window.location.pathname.split('/').pop();
  if (path === '' || path === 'index.html') {
    initIndex();
  } else if (path === 'movie.html') {
    initMovieDetail();
  } else if (path === 'admin.html') {
    initAdminPage();
  }
});

// --- FUNCIONES DE FETCH ---

async function tmdbFetch(endpoint) {
  const url = `${TMDB_BASE}${endpoint}${endpoint.includes('?') ? '&' : '?'}api_key=${TMDB_API_KEY}&language=es-ES`;
  const res = await fetch(url);
  if (!res.ok) throw new Error('TMDB fetch error: ' + res.status);
  return res.json();
}

// --- CONEXIÓN CON LA API (CON MODO DEMOSTRACIÓN) ---
// Si la API responde, el panel trabaja con la base de datos real.
// Si no responde (por ejemplo, mientras el servidor gratuito despierta),
// el panel pasa a un modo demostración que guarda los datos en el navegador.

let apiModo = 'real'; // 'real' o 'demo'

async function apiResponde(timeoutMs) {
  const control = new AbortController();
  const temporizador = setTimeout(() => control.abort(), timeoutMs);
  try {
    const res = await fetch(`${CINEX_BASE}/productos`, { signal: control.signal });
    return res.ok;
  } catch (err) {
    return false;
  } finally {
    clearTimeout(temporizador);
  }
}

function mostrarEstadoApi(html, tipo) {
  const aviso = document.getElementById('api-status');
  if (!aviso) return;
  aviso.className = `api-status api-status-${tipo}`;
  aviso.innerHTML = html;
  aviso.hidden = false;
}

async function detectarApi() {
  mostrarEstadoApi('Conectando con el servidor…', 'info');
  if (await apiResponde(8000)) {
    apiModo = 'real';
    mostrarEstadoApi(`Conectado a la API en vivo. <a href="${CINEX_BASE}/swagger-ui.html" target="_blank" rel="noopener">Ver la documentación (Swagger)</a>`, 'ok');
    return;
  }
  apiModo = 'demo';
  mostrarEstadoApi('<strong>Modo demostración:</strong> el servidor no respondió, así que los cambios se guardan solo en tu navegador. Seguimos intentando conectar…', 'demo');
  esperarApi();
}

// Reintenta durante unos minutos; si la API despierta, ofrece cambiar a los datos reales.
async function esperarApi(intento = 1) {
  if (intento > 12) {
    mostrarEstadoApi('<strong>Modo demostración:</strong> el servidor no está disponible en este momento. Los cambios se guardan solo en tu navegador.', 'demo');
    return;
  }
  await new Promise(resolver => setTimeout(resolver, 15000));
  if (await apiResponde(10000)) {
    mostrarEstadoApi('El servidor ya está disponible. <button type="button" id="usar-api-real">Usar los datos reales</button>', 'ok');
    document.getElementById('usar-api-real').addEventListener('click', () => window.location.reload());
    return;
  }
  esperarApi(intento + 1);
}

async function cinexRequest(metodo, endpoint, body) {
  if (apiModo === 'demo') return demoApi(metodo, endpoint, body);

  const opciones = { method: metodo };
  if (body !== undefined) {
    opciones.headers = { 'Content-Type': 'application/json' };
    opciones.body = JSON.stringify(body);
  }
  const res = await fetch(`${CINEX_BASE}${endpoint}`, opciones);
  const esJson = res.headers.get('content-type')?.includes('application/json');
  const datos = esJson ? await res.json() : undefined;
  if (!res.ok) {
    // La API responde errores en JSON con un mensaje claro (por ejemplo, 409).
    throw new Error(datos?.mensaje || `Error ${res.status} en ${metodo} ${endpoint}`);
  }
  return datos;
}

const cinexFetch = (endpoint) => cinexRequest('GET', endpoint);
const cinexPost = (endpoint, body) => cinexRequest('POST', endpoint, body);
const cinexPut = (endpoint, body) => cinexRequest('PUT', endpoint, body);
const cinexDelete = (endpoint) => cinexRequest('DELETE', endpoint);

// --- MODO DEMOSTRACIÓN: misma API, datos en el navegador ---

const DEMO_CLAVE = 'cinex-demo-v1';
let demoMemoria = null; // respaldo si el navegador bloquea localStorage

function demoDatosIniciales() {
  return {
    siguienteId: 100,
    usuarios: [
      { id: 1, nombre: 'Alexander', email: 'alexander@example.com' },
      { id: 2, nombre: 'Daniel', email: 'daniel@example.com' },
    ],
    productos: [
      { id: 1, nombre: 'Crispetas medianas', precio: 12000 },
      { id: 2, nombre: 'Combo crispetas + gaseosa', precio: 18000 },
    ],
    reservas: [],
  };
}

function demoLeer() {
  try {
    const guardado = localStorage.getItem(DEMO_CLAVE);
    if (guardado) return JSON.parse(guardado);
  } catch (err) { /* sin acceso a localStorage */ }
  if (!demoMemoria) demoMemoria = demoDatosIniciales();
  return demoMemoria;
}

function demoGuardar(datos) {
  demoMemoria = datos;
  try { localStorage.setItem(DEMO_CLAVE, JSON.stringify(datos)); } catch (err) { /* se queda en memoria */ }
}

async function demoApi(metodo, endpoint, body) {
  const [, recurso, idTexto] = endpoint.split('/');
  const datos = demoLeer();
  const lista = datos[recurso];
  if (!lista) throw new Error(`Recurso desconocido: ${recurso}`);
  const id = idTexto ? Number(idTexto) : null;
  const buscar = () => {
    const registro = lista.find(r => r.id === id);
    if (!registro) throw new Error(`No existe el registro ${id}`);
    return registro;
  };
  // Igual que el backend: la reserva guarda el usuario y el producto completos.
  const completar = (reserva) => ({
    ...reserva,
    usuario: datos.usuarios.find(u => u.id === Number(reserva.usuario?.id)) || null,
    producto: datos.productos.find(p => p.id === Number(reserva.producto?.id)) || null,
    tmdbMovieId: reserva.tmdbMovieId != null ? Number(reserva.tmdbMovieId) : null,
  });

  if (metodo === 'GET') return id ? structuredClone(buscar()) : structuredClone(lista);

  if (metodo === 'POST') {
    let nuevo = { ...body, id: datos.siguienteId++ };
    if (recurso === 'productos') nuevo.precio = Number(nuevo.precio);
    if (recurso === 'reservas') nuevo = completar(nuevo);
    lista.push(nuevo);
    demoGuardar(datos);
    return structuredClone(nuevo);
  }

  if (metodo === 'PUT') {
    const actual = buscar();
    let actualizado = { ...actual, ...body, id };
    if (recurso === 'productos') actualizado.precio = Number(actualizado.precio);
    if (recurso === 'reservas') actualizado = completar(actualizado);
    lista[lista.indexOf(actual)] = actualizado;
    demoGuardar(datos);
    return structuredClone(actualizado);
  }

  if (metodo === 'DELETE') {
    lista.splice(lista.indexOf(buscar()), 1);
    demoGuardar(datos);
    return undefined;
  }
  throw new Error(`Método no soportado: ${metodo}`);
}

function formatoPrecio(valor) {
  return '$' + Number(valor).toLocaleString('es-CO');
}


// --- FUNCIONES DE LA APP DE PELÍCULAS (CON FORMATO CORRECTO) ---

function shuffle(array) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
}

function excerpt(text, length = 140) {
  if (!text) return '';
  return text.length > length ? text.slice(0, length - 3) + '...' : text;
}

async function initIndex() {
  const moviesGrid = document.getElementById('movies-grid');
  const heroSlider = document.getElementById('hero-slider');
  try {
    let data = await tmdbFetch('/movie/now_playing?page=1');
    let results = data.results && data.results.length ? data.results : (await tmdbFetch('/movie/popular?page=1')).results;
    results = shuffle(results);
    const toShow = results.slice(0, 12);
    renderHero(heroSlider, toShow.slice(0, 3));
    renderMoviesGrid(moviesGrid, toShow);
  } catch (err) {
    console.error('Error cargando índice:', err);
    moviesGrid.innerHTML = '<p class="muted">No fue posible cargar las películas. Revisa la consola.</p>';
  }
}

function renderHero(container, slides) {
  container.innerHTML = '';
  slides.forEach((m, i) => {
    const div = document.createElement('div');
    div.className = 'hero-slide' + (i === 0 ? ' active' : '');
    const img = document.createElement('img');
    img.alt = m.title;
    img.loading = 'lazy';
    img.decoding = 'async';
    img.src = TMDB_IMAGE + (m.backdrop_path || m.poster_path || '');
    div.appendChild(img);
    container.appendChild(div);
  });
  let idx = 0;
  const els = container.querySelectorAll('.hero-slide');
  if (els.length <= 1) return;
  setInterval(() => {
    els[idx].classList.remove('active');
    idx = (idx + 1) % els.length;
    els[idx].classList.add('active');
  }, 6000);
}

function renderMoviesGrid(container, movies) {
  container.innerHTML = '';
  movies.forEach(m => {
    const card = document.createElement('article');
    card.className = 'movie-card';
    card.setAttribute('role', 'link');
    card.addEventListener('click', () => window.location.href = `movie.html?id=${m.id}`);
    const poster = document.createElement('div');
    poster.className = 'movie-poster';
    const img = document.createElement('img');
    img.alt = m.title;
    img.loading = 'lazy';
    img.decoding = 'async';
    img.src = TMDB_IMAGE + (m.poster_path || m.backdrop_path || '');
    poster.appendChild(img);
    const body = document.createElement('div');
    body.className = 'movie-body';
    const title = document.createElement('h3');
    title.className = 'movie-title';
    title.textContent = m.title + (m.release_date ? ` (${m.release_date.split('-')[0]})` : '');
    const meta = document.createElement('div');
    meta.className = 'movie-meta';
    meta.textContent = `⭐ ${m.vote_average || '-'} `;
    const overview = document.createElement('p');
    overview.className = 'movie-overview';
    overview.textContent = excerpt(m.overview, 160);
    body.appendChild(title);
    body.appendChild(meta);
    body.appendChild(overview);
    card.appendChild(poster);
    card.appendChild(body);
    container.appendChild(card);
  });
}

async function initMovieDetail() {
  const params = new URLSearchParams(window.location.search);
  const id = params.get('id');
  const container = document.getElementById('movie-detail');
  if (!id) {
    container.innerHTML = '<p class="muted">ID de película no especificado.</p>';
    return;
  }
  try {
    const m = await tmdbFetch(`/movie/${id}`);
    container.innerHTML = `
      <div class="detail-grid">
        <div class="detail-poster">
          <img src="${TMDB_IMAGE + (m.poster_path || m.backdrop_path || '')}" alt="${m.title}">
        </div>
        <div class="detail-meta">
          <h1>${m.title} ${m.release_date ? `(${m.release_date.split('-')[0]})` : ''}</h1>
          <div class="movie-meta">⭐ ${m.vote_average} • ${m.runtime ? m.runtime + ' min' : ''}</div>
          <div class="genres">${(m.genres || []).map(g => `<span class="genre">${g.name}</span>`).join('')}</div>
          <h3>Descripción</h3>
          <p>${m.overview || 'Sin descripción disponible.'}</p>
          <p><strong>Idioma original:</strong> ${m.original_language || 'N/A'}</p>
        </div>
      </div>
    `;
  } catch (err) {
    console.error('Error cargando detalle:', err);
    container.innerHTML = '<p class="muted">No fue posible cargar la información de la película.</p>';
  }
}


// --- FUNCIONES PARA LA PÁGINA DE ADMINISTRACIÓN (CINEX) ---

async function initAdminPage() {
  await detectarApi();
  const reservasForm = document.getElementById('reserva-form');
  const userForm = document.getElementById('user-form');
  const productForm = document.getElementById('product-form');

  try {
    // Si TMDB falla, el panel sigue funcionando (solo queda vacía la lista de películas).
    const [users, products, movies] = await Promise.all([
      cinexFetch('/usuarios'),
      cinexFetch('/productos'),
      tmdbFetch('/movie/now_playing').then(r => r.results).catch(() => [])
    ]);

    await refreshUsersList();
    await refreshProductsList();
    
    populateReservationForm(users, products, movies);

  } catch (err) {
    console.error('Error loading initial admin data:', err);
    document.getElementById('users-list').innerHTML = '<p class="muted">Error al cargar datos.</p>';
    document.getElementById('products-list').innerHTML = '<p class="muted">Error al cargar datos.</p>';
  }

  await refreshReservasList();
  
  reservasForm.addEventListener('submit', handleReservaFormSubmit);
  userForm.addEventListener('submit', handleUserFormSubmit);
  productForm.addEventListener('submit', handleProductFormSubmit);
}

// --- CRUD DE RESERVAS ---

function populateReservationForm(users, products, movies) {
  const userSelect = document.getElementById('usuario-select');
  const productSelect = document.getElementById('producto-select');
  const movieSelect = document.getElementById('movie-select');

  renderSelect(userSelect, users, user => user.nombre, user => user.id);
  renderSelect(productSelect, products, product => `${product.nombre} - ${formatoPrecio(product.precio)}`, product => product.id);
  renderSelect(movieSelect, movies, movie => movie.title, movie => movie.id);
}

async function handleReservaFormSubmit(event) {
  event.preventDefault();
  const form = event.target;
  const reservaId = form.querySelector('#reserva-id').value;
  const userId = form.querySelector('#usuario-select').value;
  const movieId = form.querySelector('#movie-select').value;
  const movieSelect = form.querySelector('#movie-select');
  const movieTitle = movieSelect.options[movieSelect.selectedIndex].text;
  const productId = form.querySelector('#producto-select').value;

  if (!userId || !productId || !movieId) {
    alert('Por favor, selecciona un usuario, una película y un producto.');
    return;
  }

  const reservaData = { usuario: { id: userId }, producto: { id: productId }, tmdbMovieId: movieId, movieTitle: movieTitle };

  try {
    if (reservaId) {
      await cinexPut(`/reservas/${reservaId}`, reservaData);
      alert('¡Reserva actualizada con éxito!');
    } else {
      await cinexPost('/reservas', reservaData);
      alert('¡Reserva creada con éxito!');
    }
    resetReservaForm();
    await refreshReservasList();
  } catch (err) {
    console.error('Error al guardar la reserva:', err);
    alert('No se pudo guardar la reserva. ' + err.message);
  }
}

async function populateFormForEdit(reservaId) {
    try {
        const reserva = await cinexFetch(`/reservas/${reservaId}`);
        document.getElementById('reserva-id').value = reserva.id;
        document.getElementById('usuario-select').value = reserva.usuario.id;
        document.getElementById('producto-select').value = reserva.producto.id;
        document.getElementById('movie-select').value = reserva.tmdbMovieId;
        document.getElementById('reserva-submit-btn').textContent = 'Actualizar Reserva';
        document.getElementById('reserva-form').scrollIntoView({ behavior: 'smooth' });
    } catch (err) {
        console.error('Error al cargar la reserva para editar:', err);
        alert('No se pudo cargar la reserva. Revisa la consola.');
    }
}

function resetReservaForm() {
    const form = document.getElementById('reserva-form');
    form.reset();
    form.querySelector('#reserva-id').value = '';
    document.getElementById('reserva-submit-btn').textContent = 'Crear Reserva';
}

async function handleDeleteReserva(reservaId) {
  if (!confirm(`¿Estás seguro de que quieres eliminar la reserva con ID ${reservaId}?`)) return;
  try {
    await cinexDelete(`/reservas/${reservaId}`);
    alert('Reserva eliminada con éxito.');
    await refreshReservasList();
  } catch (err) {
    console.error('Error al eliminar la reserva:', err);
    alert('No se pudo eliminar la reserva. ' + err.message);
  }
}

async function refreshReservasList() {
  const reservasList = document.getElementById('reservas-list');
  reservasList.innerHTML = '<li>Cargando reservas...</li>';
  try {
    const reservas = await cinexFetch('/reservas');
    reservasList.innerHTML = '';
    if (!reservas || reservas.length === 0) {
      reservasList.innerHTML = '<li>No hay reservas para mostrar.</li>';
      return;
    }
    reservas.forEach(reserva => {
      const li = document.createElement('li');
      const userName = reserva.usuario ? reserva.usuario.nombre : 'N/A';
      const productName = reserva.producto ? reserva.producto.nombre : 'N/A';
      const movieTitle = reserva.movieTitle || 'Película no especificada';
      
      const text = document.createElement('span');
      text.textContent = `${userName} reservó "${movieTitle}" con ${productName}. `;
      li.appendChild(text);
      
      const actionsDiv = document.createElement('div');
      actionsDiv.className = 'list-actions';

      const editButton = document.createElement('button');
      editButton.textContent = 'Editar';
      editButton.className = 'edit-btn';
      editButton.addEventListener('click', () => populateFormForEdit(reserva.id));
      actionsDiv.appendChild(editButton);

      const deleteButton = document.createElement('button');
      deleteButton.textContent = 'Eliminar';
      deleteButton.className = 'delete-btn';
      deleteButton.addEventListener('click', () => handleDeleteReserva(reserva.id));
      actionsDiv.appendChild(deleteButton);

      li.appendChild(actionsDiv);
      reservasList.appendChild(li);
    });
  } catch (err) {
    console.error('Error loading reservas:', err);
    reservasList.innerHTML = '<p class="muted">No fue posible cargar las reservas.</p>';
  }
}

// --- CRUD DE USUARIOS ---

async function handleUserFormSubmit(event) {
    event.preventDefault();
    const form = event.target;
    const userId = form.querySelector('#user-id').value;
    const nombre = form.querySelector('#user-nombre').value;
    const email = form.querySelector('#user-email').value;

    const userData = { nombre, email };

    try {
        if (userId) {
            await cinexPut(`/usuarios/${userId}`, userData);
            alert('¡Usuario actualizado con éxito!');
        } else {
            await cinexPost('/usuarios', userData);
            alert('¡Usuario creado con éxito!');
        }
        resetUserForm();
        await refreshUsersList();
        const users = await cinexFetch('/usuarios');
        const products = await cinexFetch('/productos');
        const movies = await tmdbFetch('/movie/now_playing').then(r => r.results).catch(() => []);
        populateReservationForm(users, products, movies);
    } catch (err) {
        console.error('Error al guardar el usuario:', err);
        alert('No se pudo guardar el usuario. ' + err.message);
    }
}

async function populateUserFormForEdit(userId) {
    try {
        const user = await cinexFetch(`/usuarios/${userId}`);
        document.getElementById('user-id').value = user.id;
        document.getElementById('user-nombre').value = user.nombre;
        document.getElementById('user-email').value = user.email;
        document.getElementById('user-submit-btn').textContent = 'Actualizar Usuario';
        document.getElementById('user-form').scrollIntoView({ behavior: 'smooth' });
    } catch (err) {
        console.error('Error al cargar el usuario para editar:', err);
        alert('No se pudo cargar el usuario. Revisa la consola.');
    }
}

function resetUserForm() {
    const form = document.getElementById('user-form');
    form.reset();
    form.querySelector('#user-id').value = '';
    document.getElementById('user-submit-btn').textContent = 'Crear Usuario';
}

async function handleDeleteUser(userId) {
    if (!confirm(`¿Estás seguro de que quieres eliminar al usuario con ID ${userId}? Esta acción podría afectar a las reservas existentes.`)) return;
    try {
        await cinexDelete(`/usuarios/${userId}`);
        alert('Usuario eliminado con éxito.');
        await refreshUsersList();
        await refreshReservasList();
    } catch (err) {
        console.error('Error al eliminar el usuario:', err);
        alert('No se pudo eliminar el usuario. ' + err.message);
    }
}

async function refreshUsersList() {
    const usersList = document.getElementById('users-list');
    usersList.innerHTML = '<li>Cargando usuarios...</li>';
    try {
        const users = await cinexFetch('/usuarios');
        usersList.innerHTML = '';
        if (!users || users.length === 0) {
            usersList.innerHTML = '<li>No hay usuarios para mostrar.</li>';
            return;
        }
        users.forEach(user => {
            const li = document.createElement('li');
            const text = document.createElement('span');
            text.textContent = `${user.nombre} (${user.email}) `;
            li.appendChild(text);

            const actionsDiv = document.createElement('div');
            actionsDiv.className = 'list-actions';

            const editButton = document.createElement('button');
            editButton.textContent = 'Editar';
            editButton.className = 'edit-btn';
            editButton.addEventListener('click', () => populateUserFormForEdit(user.id));
            actionsDiv.appendChild(editButton);

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Eliminar';
            deleteButton.className = 'delete-btn';
            deleteButton.addEventListener('click', () => handleDeleteUser(user.id));
            actionsDiv.appendChild(deleteButton);

            li.appendChild(actionsDiv);
            usersList.appendChild(li);
        });
    } catch (err) {
        console.error('Error loading users:', err);
        usersList.innerHTML = '<p class="muted">No fue posible cargar los usuarios.</p>';
    }
}

// --- CRUD DE PRODUCTOS ---

async function handleProductFormSubmit(event) {
    event.preventDefault();
    const form = event.target;
    const productId = form.querySelector('#product-id').value;
    const nombre = form.querySelector('#product-nombre').value;
    const precio = form.querySelector('#product-precio').value;

    const productData = { nombre, precio };

    try {
        if (productId) {
            await cinexPut(`/productos/${productId}`, productData);
            alert('¡Producto actualizado con éxito!');
        } else {
            await cinexPost('/productos', productData);
            alert('¡Producto creado con éxito!');
        }
        resetProductForm();
        await refreshProductsList();
        const users = await cinexFetch('/usuarios');
        const products = await cinexFetch('/productos');
        const movies = await tmdbFetch('/movie/now_playing').then(r => r.results).catch(() => []);
        populateReservationForm(users, products, movies);
    } catch (err) {
        console.error('Error al guardar el producto:', err);
        alert('No se pudo guardar el producto. ' + err.message);
    }
}

async function populateProductFormForEdit(productId) {
    try {
        const product = await cinexFetch(`/productos/${productId}`);
        document.getElementById('product-id').value = product.id;
        document.getElementById('product-nombre').value = product.nombre;
        document.getElementById('product-precio').value = product.precio;
        document.getElementById('product-submit-btn').textContent = 'Actualizar Producto';
        document.getElementById('product-form').scrollIntoView({ behavior: 'smooth' });
    } catch (err) {
        console.error('Error al cargar el producto para editar:', err);
        alert('No se pudo cargar el producto. Revisa la consola.');
    }
}

function resetProductForm() {
    const form = document.getElementById('product-form');
    form.reset();
    form.querySelector('#product-id').value = '';
    document.getElementById('product-submit-btn').textContent = 'Crear Producto';
}

async function handleDeleteProduct(productId) {
    if (!confirm(`¿Estás seguro de que quieres eliminar el producto con ID ${productId}? Esta acción podría afectar a las reservas existentes.`)) return;
    try {
        await cinexDelete(`/productos/${productId}`);
        alert('Producto eliminado con éxito.');
        await refreshProductsList();
        await refreshReservasList();
    } catch (err) {
        console.error('Error al eliminar el producto:', err);
        alert('No se pudo eliminar el producto. ' + err.message);
    }
}

async function refreshProductsList() {
    const productsList = document.getElementById('products-list');
    productsList.innerHTML = '<li>Cargando productos...</li>';
    try {
        const products = await cinexFetch('/productos');
        productsList.innerHTML = '';
        if (!products || products.length === 0) {
            productsList.innerHTML = '<li>No hay productos para mostrar.</li>';
            return;
        }
        products.forEach(product => {
            const li = document.createElement('li');
            const text = document.createElement('span');
            text.textContent = `${product.nombre} - ${formatoPrecio(product.precio)} `;
            li.appendChild(text);

            const actionsDiv = document.createElement('div');
            actionsDiv.className = 'list-actions';

            const editButton = document.createElement('button');
            editButton.textContent = 'Editar';
            editButton.className = 'edit-btn';
            editButton.addEventListener('click', () => populateProductFormForEdit(product.id));
            actionsDiv.appendChild(editButton);

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Eliminar';
            deleteButton.className = 'delete-btn';
            deleteButton.addEventListener('click', () => handleDeleteProduct(product.id));
            actionsDiv.appendChild(deleteButton);

            li.appendChild(actionsDiv);
            productsList.appendChild(li);
        });
    } catch (err) {
        console.error('Error loading products:', err);
        productsList.innerHTML = '<p class="muted">No fue posible cargar los productos.</p>';
    }
}


// --- FUNCIONES GENÉRICAS ---

function renderSelect(selectElement, items, textFormatter, valueFormatter) {
  selectElement.innerHTML = '<option value="">-- Selecciona una opción --</option>';
  if (!items) return;
  items.forEach(item => {
    const option = document.createElement('option');
    option.textContent = textFormatter(item);
    option.value = valueFormatter(item);
    selectElement.appendChild(option);
  });
}