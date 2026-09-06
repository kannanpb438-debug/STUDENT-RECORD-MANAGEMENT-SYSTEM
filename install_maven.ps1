$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
$zipPath = "$env:TEMP\apache-maven.zip"
$targetDir = "$env:USERPROFILE\.maven"

if (-not (Test-Path "$targetDir\apache-maven-3.9.6\bin\mvn.cmd")) {
    Write-Host "Downloading Apache Maven 3.9.6..."
    Invoke-WebRequest -Uri $mavenUrl -OutFile $zipPath
    Write-Host "Extracting Apache Maven..."
    New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
    Expand-Archive -Path $zipPath -DestinationPath $targetDir -Force
    Remove-Item $zipPath -Force
}

Write-Host "Maven installed successfully at:"
Get-Item "$targetDir\apache-maven-3.9.6\bin\mvn.cmd"
