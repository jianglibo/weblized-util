$here = Split-Path -Parent $MyInvocation.MyCommand.Path
. "${here}\SshInvoker.ps1"
function Install-Pwsh {
    param (
        [Parameter(Mandatory=$false,Position=1)][string]$targetOs = "Centos",
        [Parameter(Mandatory=$false,Position=1)][string]$user = "root",
        [Parameter(Mandatory=$false,Position=1)][string]$HostName,
        [Parameter(Mandatory=$true,Position=1)][string]$ifile
    )

    $sshInvoker = [SshInvoker]::new($HostName, $ifile)
    switch ($targetOs) {
        "Centos" {
            if (-not $sshInvoker.IsPwshInstalled()) {
                $sshInvoker.InvokeBash('curl https://packages.microsoft.com/config/rhel/7/prod.repo | tee /etc/yum.repos.d/microsoft.repo')
                if ($sshInvoker.ExitZero()) {
                    $sshInvoker.InvokeBash('yum install -y powershell') | Out-Null
                } else {
                    'Install Failed.' | Out-Host
                }
            } else {
                'Already Installed.' | Out-Host
            }
        }
        Default {}
    }
}

function Add-Pwsh-User {
    param (
        [Parameter(Mandatory=$false,Position=1)][string]$targetOs
    )
    
}