# Phase 2 File Generation Script
# This script generates all remaining backend files for Phase 2

$baseDir = "src/main/java/com/memoreel"

# Function to create directory if it doesn't exist
function Ensure-Directory {
    param($path)
    if (!(Test-Path $path)) {
        New-Item -ItemType Directory -Path $path -Force | Out-Null
    }
}

Write-Host "Generating Phase 2 Backend Files..." -ForegroundColor Green

# Create all necessary directories
$directories = @(
    "$baseDir/project/dto",
    "$baseDir/media/dto",
    "$baseDir/video/dto",
    "$baseDir/public_link/dto",
    "$baseDir/qr/dto",
    "$baseDir/common/exception",
    "$baseDir/common/util",
    "$baseDir/security",
    "$baseDir/config"
)

foreach ($dir in $directories) {
    Ensure-Directory $dir
}

Write-Host "All directories created successfully!" -ForegroundColor Green
Write-Host "Please run the individual file creation commands to generate all files." -ForegroundColor Yellow

# Made with Bob
