class DeployConfig {
    [string]$deployDir
    [string]$tmpDir
    [string]$scriptDir
    [string]$workingDir
    [string]$pidFile
    [string]$unzipDir
    [string]$startSh
    [string]$uploaded
    [string]$rollback
    [string]$runningps1
    $config

    DeployConfig($config) {
        $this.config = $config
        $this.deployDir = $config.basedir + "/deploys"
        $this.tmpDir = $config.basedir + "/tmp"
        $this.unzipDir = $config.basedir + "/tmp/ddd"
        $this.scriptDir = $config.basedir + "/scripts"
        $this.runningps1 = $config.basedir + "/scripts/running-at-server.ps1"
        $this.workingDir = $config.basedir + "/deploys/working"
        $this.rollback = $config.basedir + "/deploys/rollback"
        $this.pidFile = $config.basedir + "/deploys/working/app.pid"
        $this.startSh = $config.basedir + "/deploys/working/start.sh"
    }
}

function Get-DeployConfig {
    param (
        [Parameter(Mandatory = $true, Position = 0)][string]$ConfigFile
    )
    $e = Test-Path $ConfigFile
    if (-not $e) {
        throw 1001
    }
    $c = Get-Content $ConfigFile | ConvertFrom-Json
    [DeployConfig]::new($c)
}