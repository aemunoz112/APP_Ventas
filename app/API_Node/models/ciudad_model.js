const db = require('../config/dbConfig');
// se llama a la configuracion de la bd

class ciudad {
  
  getCiudades(callback) {
    const sql = `
      SELECT 
        c.id,
        c.nombre,
        c.es_capital,
        c.departamento_id,
        d.nombre as departamento
      FROM ciudades c
      INNER JOIN departamentos d ON c.departamento_id = d.id
      ORDER BY c.nombre
    `;
    db.query(sql, callback);
  }

  getCiudadById(id, callback) {
    const sql = `
      SELECT 
        c.id,
        c.nombre,
        c.es_capital,
        c.departamento_id,
        d.nombre as departamento
      FROM ciudades c
      INNER JOIN departamentos d ON c.departamento_id = d.id
      WHERE c.id = ?
    `;
    db.query(sql, [id], callback);
  }

  getCiudadesByDepartamento(departamentoId, callback) {
    const sql = `
      SELECT 
        c.id,
        c.nombre,
        c.es_capital
      FROM ciudades c
      WHERE c.departamento_id = ?
      ORDER BY c.nombre
    `;
    db.query(sql, [departamentoId], callback);
  }

  searchCiudades(termino, callback) {
    const sql = `
      SELECT 
        c.id,
        c.nombre,
        c.es_capital,
        c.departamento_id,
        d.nombre as departamento
      FROM ciudades c
      INNER JOIN departamentos d ON c.departamento_id = d.id
      WHERE c.nombre LIKE ?
      ORDER BY c.nombre
    `;
    db.query(sql, [`%${termino}%`], callback);
  }

}

module.exports = new ciudad();