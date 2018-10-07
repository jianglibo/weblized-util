param (
        [Parameter(Mandatory=$false,Position=1)][string]$targetOs = "Centos",
        [Parameter(Mandatory=$false,Position=1)][string]$user = "root",
        [Parameter(Mandatory=$false,Position=1)][string]$HostName,
        [Parameter(Mandatory=$true,Position=1)][string]$ifile
)

$here = Split-Path -Parent $MyInvocation.MyCommand.Path
. "$here\internal\install-pwsh-fn.ps1"

Install-Pwsh -targetOs $targetOs -user $user -HostName $HostName -ifile $ifile