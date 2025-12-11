const db = require('../config/dbConfig')


class departamento{

    getDepartamentos(callback) {
        const sql = "SELECT id, nombre FROM departamentos ORDER BY nombre";
        db.query(sql, callback);
    }

    getDepartamentoById(id, callback){
        const sql = "SELECT id, nombre from departamentos WHERE id = ?";
        db.query(sql, [id], callback)
    }

    getDepartamentoById(id, callback) {
        const sql = "SELECT id, nombre FROM departamentos WHERE id = ?";
        db.query(sql, [id], callback);
    }

  getDepartamentoConCiudades(id, callback) {
    const sql = `
      SELECT 
        d.id as departamento_id,
        d.nombre as departamento,
        c.id as ciudad_id,
        c.nombre as ciudad,
        c.es_capital
      FROM departamentos d
      LEFT JOIN ciudades c ON d.id = c.departamento_id
      WHERE d.id = ?
      ORDER BY c.nombre
    `;
    db.query(sql, [id], callback);
  }

}

module.exports = new departamento();