@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  DacWizard startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and DAC_WIZARD_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\DacWizard.jar;%APP_HOME%\lib\jme3-utilities-nifty-0.9.18.jar;%APP_HOME%\lib\jme3-utilities-ui-0.9.2.jar;%APP_HOME%\lib\Wes-0.6.2.jar;%APP_HOME%\lib\Minie-4.1.0-SNAPSHOT.jar;%APP_HOME%\lib\Heart-6.4.2.jar;%APP_HOME%\lib\nifty-style-black-1.4.3.jar;%APP_HOME%\lib\jme3-lwjgl-3.3.2-stable.jar;%APP_HOME%\lib\jme3-blender-3.3.2-stable.jar;%APP_HOME%\lib\jme3-desktop-3.3.2-stable.jar;%APP_HOME%\lib\jme3-jogg-3.3.2-stable.jar;%APP_HOME%\lib\jme3-plugins-3.3.2-stable.jar;%APP_HOME%\lib\jme3-terrain-3.3.2-stable.jar;%APP_HOME%\lib\jme3-effects-3.3.2-stable.jar;%APP_HOME%\lib\jme3-core-3.3.2-stable.jar;%APP_HOME%\lib\nifty-default-controls-1.4.3.jar;%APP_HOME%\lib\nifty-1.4.3.jar;%APP_HOME%\lib\lwjgl-2.9.3.jar;%APP_HOME%\lib\simple-0.27.jar;%APP_HOME%\lib\dense64-0.27.jar;%APP_HOME%\lib\denseC64-0.27.jar;%APP_HOME%\lib\core-0.27.jar;%APP_HOME%\lib\j-ogg-all-1.0.0.jar;%APP_HOME%\lib\gson-2.8.1.jar;%APP_HOME%\lib\xpp3-1.1.4c.jar;%APP_HOME%\lib\jsr305-2.0.2.jar;%APP_HOME%\lib\lwjgl-platform-2.9.3-natives-windows.jar;%APP_HOME%\lib\lwjgl-platform-2.9.3-natives-linux.jar;%APP_HOME%\lib\lwjgl-platform-2.9.3-natives-osx.jar;%APP_HOME%\lib\jinput-2.0.5.jar;%APP_HOME%\lib\jutils-1.0.0.jar;%APP_HOME%\lib\jinput-platform-2.0.5-natives-linux.jar;%APP_HOME%\lib\jinput-platform-2.0.5-natives-windows.jar;%APP_HOME%\lib\jinput-platform-2.0.5-natives-osx.jar


@rem Execute DacWizard
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DAC_WIZARD_OPTS%  -classpath "%CLASSPATH%" jme3utilities.minie.wizard.DacWizard %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable DAC_WIZARD_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%DAC_WIZARD_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
