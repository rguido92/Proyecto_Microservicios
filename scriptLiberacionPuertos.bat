@echo off
net session >nul 2>&1
if %errorlevel% neq 0 (
    powershell -NoProfile -ExecutionPolicy Bypass -Command "Start-Process '%~f0' -Verb RunAs"
    exit /b
)

powershell -NoProfile -ExecutionPolicy Bypass -Command ^
"$ports = @(3306,27017,8080,8700,8701,8702,8703); ^
foreach ($port in $ports) { ^
    $lines = netstat -ano ^| Select-String ('\:' + $port + ' '); ^
    if (-not $lines) { ^
        Write-Host ('Puerto ' + $port + ' libre'); ^
        continue ^
    } ^
    $portPids = $lines ^| ForEach-Object { ($_ -split '\s+')[-1] } ^| Where-Object { $_ -match '^\d+$' -and $_ -ne '0' } ^| Sort-Object -Unique; ^
    foreach ($procId in $portPids) { ^
        try { ^
            $proc = Get-Process -Id $procId -ErrorAction Stop; ^
            Write-Host ''; ^
            Write-Host ('Puerto: ' + $port); ^
            Write-Host ('PID: ' + $procId); ^
            Write-Host ('Proceso: ' + $proc.ProcessName); ^
            $answer = Read-Host 'Detener este proceso? (s/n)'; ^
            if ($answer -eq 's') { ^
                Stop-Process -Id $procId -Force; ^
                Write-Host ('PID ' + $procId + ' detenido'); ^
            } else { ^
                Write-Host ('PID ' + $procId + ' omitido'); ^
            } ^
        } catch { ^
            Write-Host ('No se pudo inspeccionar o detener PID ' + $procId + ' en puerto ' + $port); ^
        } ^
    } ^
}"
pause