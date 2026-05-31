@echo off
echo ===================================================
echo Killing existing processes on project ports...
echo ===================================================

powershell -Command "$ports = @(8761, 8080, 8081, 8082, 3000); $lines = netstat -ano; foreach ($port in $ports) { $matches = $lines | Select-String (':{0}\s+' -f $port); foreach ($match in $matches) { $parts = $match.Line -split '\s+' | Where-Object { $_ }; if ($parts.Length -ge 4) { $procId = $parts[-1]; if ($procId -match '^\d+$' -and $procId -ne '0' -and $procId -ne '4') { Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue } } } }"

echo Port cleanup complete!
