const express = require('express');
const router = express.Router();

const departamentoController = require('../controllers/departamento_controller');

router.get('/getDepartamentos', departamentoController.getDepartamentos);
router.get('/getDepartamentoById/:id', departamentoController.getDepartamentoById);
router.get('/getDepartamentoConCiudades/:id', departamentoController.getDepartamentoConCiudades);


module.exports = router;