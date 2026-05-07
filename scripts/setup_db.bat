@echo off
setlocal

cd /d "%~dp0\.."

if "%~1"=="" (
    set "DB_FILE=%CD%\database\auction.fdb"
) else (
    set "DB_FILE=%~1"
)

for %%I in ("%DB_FILE%") do (
    set "DB_FILE=%%~fI"
    set "DB_DIR=%%~dpI"
)

set "SCHEMA_FILE=%CD%\src\main\resources\db\schema-red.sql"
set "DATA_FILE=%CD%\src\main\resources\db\demo-data.sql"
set "TMP_SQL=%TEMP%\auction_create_db.sql"

if not exist "%DB_DIR%" mkdir "%DB_DIR%"

if exist "%DB_FILE%" (
    echo Database file already exists:
    echo %DB_FILE%
    exit /b 1
)

if not exist "%SCHEMA_FILE%" (
    echo File not found:
    echo %SCHEMA_FILE%
    exit /b 1
)

if not exist "%DATA_FILE%" (
    echo File not found:
    echo %DATA_FILE%
    exit /b 1
)

where isql >nul 2>nul
if errorlevel 1 (
    echo isql not found in PATH
    echo Add Firebird or RED Database bin folder to PATH
    exit /b 1
)

echo CREATE DATABASE 'localhost:%DB_FILE%' USER 'SYSDBA' PASSWORD 'masterkey'; > "%TMP_SQL%"
echo quit; >> "%TMP_SQL%"

echo Creating database...
isql -i "%TMP_SQL%"
if errorlevel 1 (
    del "%TMP_SQL%" >nul 2>nul
    echo Database creation failed
    exit /b 1
)

echo Applying schema...
isql -bail -user SYSDBA -password masterkey "%DB_FILE%" -i "%SCHEMA_FILE%"
if errorlevel 1 (
    del "%TMP_SQL%" >nul 2>nul
    echo Schema import failed
    exit /b 1
)

echo Applying demo data...
isql -bail -user SYSDBA -password masterkey "%DB_FILE%" -i "%DATA_FILE%"
if errorlevel 1 (
    del "%TMP_SQL%" >nul 2>nul
    echo Demo data import failed
    exit /b 1
)

del "%TMP_SQL%" >nul 2>nul

echo.
echo Database created successfully:
echo %DB_FILE%