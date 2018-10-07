$here = $MyInvocation.MyCommand.Path
. "$($here | Split-Path -Parent )\ivc.1.ps1"

function ivc-one-infunc {
    $MyInvocation.MyCommand.Path
}

function ivc-one-fromtop {
    $here
}

function ivc-nested {
    ivc-one-fromtop-nest
}