# Script para iniciar el backend correctamente
# Ejecutar: .\iniciar_backend.ps1

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  INICIANDO BACKEND FASTAPI" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

$proyectoPath = "C:\Proyecto_Ventas"

if (-not (Test-Path $proyectoPath)) {
    Write-Host "✗ No se encuentra la ruta: $proyectoPath" -ForegroundColor Red
    Write-Host ""
    Write-Host "Cambia la ruta en este script (línea 7)" -ForegroundColor Yellow
    Write-Host "O ejecuta manualmente desde tu carpeta del proyecto:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "cd TU_RUTA_DEL_BACKEND" -ForegroundColor White
    Write-Host ".\venv\Scripts\Activate.ps1" -ForegroundColor White
    Write-Host "uvicorn app.main:app --reload --host 0.0.0.0 --port 8000" -ForegroundColor White
    exit
}

Write-Host "✓ Ruta del proyecto: $proyectoPath" -ForegroundColor Green
Write-Host ""

# Cambiar al directorio del proyecto
Set-Location $proyectoPath

# Activar entorno virtual
Write-Host "Activando entorno virtual..." -ForegroundColor Yellow
& ".\venv\Scripts\Activate.ps1"

Write-Host ""
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  BACKEND INICIADO" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Tu IP WiFi: 192.168.1.88" -ForegroundColor Cyan
Write-Host ""
Write-Host "Accede desde:" -ForegroundColor Yellow
Write-Host "  PC:      http://localhost:8000/docs" -ForegroundColor White
Write-Host "  Celular: http://192.168.1.88:8000/docs" -ForegroundColor White
Write-Host ""
Write-Host "Presiona Ctrl+C para detener el servidor" -ForegroundColor Yellow
Write-Host ""
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# Iniciar servidor
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000

