# API de Productos - Endpoints Disponibles

## ✅ Servidor Activo
- **URL Base:** `http://localhost:8083`
- **Puerto:** 8083
- **CORS:** Habilitado para `http://localhost:5173`

## 📋 Endpoints Disponibles

### Productos

#### Obtener todos los productos
```
GET http://localhost:8083/api/catalog/productos
```
**Respuesta:** Array de 17 productos con sus categorías

#### Filtrar productos por categoría
```
GET http://localhost:8083/api/catalog/productos?categoria=cuadros
GET http://localhost:8083/api/catalog/productos?categoria=grecas
GET http://localhost:8083/api/catalog/productos?categoria=rusticas
GET http://localhost:8083/api/catalog/productos?categoria=nativas
GET http://localhost:8083/api/catalog/productos?categoria=finger-joint
GET http://localhost:8083/api/catalog/productos?categoria=naturales
```

#### Obtener un producto por ID
```
GET http://localhost:8083/api/catalog/productos/{id}
```

#### Crear un producto
```
POST http://localhost:8083/api/catalog/productos
Content-Type: application/json

{
  "nombre": "Nombre del producto",
  "descripcion": "Descripción del producto",
  "precio": 20000.0,
  "stock": 50,
  "imagenUrl": "/assets/imagen.jpg",
  "categoriaId": 1
}
```

#### Actualizar un producto
```
PUT http://localhost:8083/api/catalog/productos/{id}
Content-Type: application/json

{
  "nombre": "Nombre actualizado",
  "descripcion": "Nueva descripción",
  "precio": 25000.0,
  "stock": 30,
  "imagenUrl": "/assets/nueva-imagen.jpg",
  "categoriaId": 1
}
```

#### Eliminar un producto
```
DELETE http://localhost:8083/api/catalog/productos/{id}
```

### Categorías

#### Obtener todas las categorías
```
GET http://localhost:8083/api/catalog/categorias
```
**Respuesta:**
```json
[
  {"id": 1, "nombre": "grecas", "descripcion": "Molduras con diseños clásicos"},
  {"id": 2, "nombre": "rusticas", "descripcion": "Estilo madera envejecida"},
  {"id": 3, "nombre": "nativas", "descripcion": "Maderas nobles chilenas"},
  {"id": 4, "nombre": "finger-joint", "descripcion": "Unión dentada resistente"},
  {"id": 5, "nombre": "naturales", "descripcion": "Molduras naturales con textura de madera"},
  {"id": 6, "nombre": "cuadros", "descripcion": "Marcos y cuadros decorativos"}
]
```

#### Crear una categoría
```
POST http://localhost:8083/api/catalog/categorias
Content-Type: application/json

{
  "nombre": "nueva-categoria",
  "descripcion": "Descripción de la categoría"
}
```

### Upload de Imágenes

```
POST http://localhost:8083/api/catalog/upload
Content-Type: multipart/form-data

file: [archivo de imagen]
```

## 🔧 Ejemplo de uso en Frontend (TypeScript/JavaScript)

### Obtener todos los productos
```typescript
const response = await fetch('http://localhost:8083/api/catalog/productos', {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json',
  },
  credentials: 'include', // Para enviar cookies si es necesario
});

const productos = await response.json();
console.log('Productos:', productos);
```

### Filtrar productos por categoría
```typescript
const categoria = 'cuadros';
const response = await fetch(`http://localhost:8083/api/catalog/productos?categoria=${categoria}`, {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json',
  },
});

const cuadros = await response.json();
console.log('Cuadros:', cuadros);
```

### Crear un producto
```typescript
const nuevoProducto = {
  nombre: "Marco Elegante",
  descripcion: "Marco de alta calidad",
  precio: 30000.0,
  stock: 20,
  imagenUrl: "/assets/marco-elegante.jpg",
  categoriaId: 6
};

const response = await fetch('http://localhost:8083/api/catalog/productos', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(nuevoProducto),
});

const productoCreado = await response.json();
console.log('Producto creado:', productoCreado);
```

## ⚠️ Errores Comunes

### Error: "Failed to fetch" o "Network error"
**Causa:** El servidor no está corriendo o la URL es incorrecta.
**Solución:** 
1. Verifica que el servidor esté en http://localhost:8083
2. Asegúrate de usar `/api/catalog/productos` (no `/api/productos`)

### Error: CORS
**Causa:** El origen del frontend no está permitido.
**Solución:** El servidor ya está configurado para `http://localhost:5173`

### Error: 404 Not Found
**Causa:** Ruta incorrecta.
**Solución:** 
- ✅ Correcto: `http://localhost:8083/api/catalog/productos`
- ❌ Incorrecto: `http://localhost:8083/api/productos`
- ❌ Incorrecto: `http://localhost:8083/productos`

## 📊 Productos Actuales en la Base de Datos

Total: **17 productos** distribuidos en **6 categorías**

- **grecas**: 5 productos
- **rusticas**: 1 producto
- **nativas**: 1 producto
- **finger-joint**: 1 producto
- **naturales**: 1 producto
- **cuadros**: 5 productos
