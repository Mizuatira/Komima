$ErrorActionPreference = "Stop"

$MVNW_DIR = "$PSScriptRoot\.mvn\wrapper"
$MVNW_JAR = "$MVNW_DIR\maven-wrapper.jar"
$MVNW_PROPS = "$MVNW_DIR\maven-wrapper.properties"
$MVNW_URL = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

$JAVA_EXE = (Get-Command java).Source

$props = @{}
Get-Content $MVNW_PROPS | ForEach-Object {
    $line = $_.Trim()
    if ($line -match '^(\S+)\s*=\s*(.+)$') {
        $props[$Matches[1]] = $Matches[2]
    }
}

if (-not (Test-Path $MVNW_JAR)) {
    Write-Host "Downloading Maven Wrapper..." -ForegroundColor Yellow
    $wrapperUrl = if ($props.ContainsKey("wrapperUrl")) { $props["wrapperUrl"] } else { $MVNW_URL }
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $wrapperUrl -OutFile $MVNW_JAR
    Write-Host "Maven Wrapper downloaded." -ForegroundColor Green
}

$distUrl = $props["distributionUrl"]
$mavenHome = "$env:USERPROFILE\.m2\wrapper\dists\" + ($distUrl -replace '[^\w\-\.]+', '_')

if (-not (Test-Path "$mavenHome\bin\mvn.cmd")) {
    Write-Host "Downloading Maven distribution..." -ForegroundColor Yellow
    $zipPath = "$env:TEMP\maven-dist.zip"
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $distUrl -OutFile $zipPath
    Expand-Archive -Path $zipPath -DestinationPath $mavenHome -Force
    Remove-Item $zipPath -Force
    $extractedDir = Get-ChildItem $mavenHome -Directory | Select-Object -First 1
    Get-ChildItem "$($extractedDir.FullName)\*" | Move-Item -Destination $mavenHome -Force
    $extractedDir | Remove-Item -Force
    Write-Host "Maven distribution ready." -ForegroundColor Green
}

$env:JAVA_HOME = Split-Path -Parent (Split-Path -Parent $JAVA_EXE)
$env:MAVEN_PROJECTBASEDIR = $PSScriptRoot

& "$mavenHome\bin\mvn.cmd" @args
