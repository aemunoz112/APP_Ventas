const departamentoModel = require('../models/departamento_model');

module.exports = {

  getDepartamentos: (req, res) => {
    departamentoModel.getDepartamentos((err, result) => {
      if (err) {
        res.status(500).json({ error: err.message });
        return;
      }
      res.status(200).json({ data: result });
    });
  },

  getDepartamentoById: (req, res) => {
    const { id } = req.params;
    departamentoModel.getDepartamentoById(id, (err, result) => {
      if (err) {
        res.status(500).json({ error: err.message });
        return;
      }
      
      if (result.length === 0) {
        res.status(404).json({ message: 'Departamento no encontrado' });
        return;
      }

      res.status(200).json({ data: result });
    });
  },

  getDepartamentoConCiudades: (req, res) => {
    const { id } = req.params;
    departamentoModel.getDepartamentoConCiudades(id, (err, result) => {
      if (err) {
        res.status(500).json({ error: err.message });
        return;
      }
      
      if (result.length === 0) {
        res.status(404).json({ message: 'Departamento no encontrado' });
        return;
      }

      // Estructurar la respuesta
      const departamento = {
        id: result[0].departamento_id,
        nombre: result[0].departamento,
        ciudades: result[0].ciudad_id ? result.map(row => ({
          id: row.ciudad_id,
          nombre: row.ciudad,
          es_capital: row.es_capital
        })) : []
      };

      res.status(200).json({ data: departamento });
    });
  },

};