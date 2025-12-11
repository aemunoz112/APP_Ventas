const express = require('express');
const router = express.Router();

const ciudadController = require('../controllers/ciudad_controller');

router.get('/getCiudades', ciudadController.getCiudades);
router.get('/getCiudadById/:id', ciudadController.getCiudadById);
router.get('/getCiudadesByDepartamento/:departamentoId', ciudadController.getCiudadesByDepartamento);
router.get('/searchCiudades/:termino', ciudadController.searchCiudades);


module.exports = router;