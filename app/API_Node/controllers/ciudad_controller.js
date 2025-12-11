const ciudadModel = require('../models/ciudad_model');

module.exports = {

  getCiudades: (req, res) => {
    ciudadModel.getCiudades((err, result) => {
      if (err) {
        res.status(500).json({ error: err.message });
        return;
      }
      res.status(200).json({ data: result });
    });
  },

  getCiudadById: (req, res) => {
    const { id } = req.params;
    ciudadModel.getCiudadById(id, (err, result) => {
      if (err) {
        res.status(500).json({ error: err.message });
        return;
      }
      
      if (result.length === 0) {
        res.status(404).json({ message: 'Ciudad no encontrada' });
        return;
      }

      res.status(200).json({ data: result });
    });
  },

  getCiudadesByDepartamento: (req, res) => {
    const { departamentoId } = req.params;
    ciudadModel.getCiudadesByDepartamento(departamentoId, (err, result) => {
      if (err) {
        res.status(500).json({ error: err.message });
        return;
      }
      res.status(200).json({ data: result });
    });
  },

  searchCiudades: (req, res) => {
    const { termino } = req.params;
    ciudadModel.searchCiudades(termino, (err, result) => {
      if (err) {
        res.status(500).json({ error: err.message });
        return;
      }
      res.status(200).json({ data: result });
    });
  }

};