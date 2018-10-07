$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$sut = (Split-Path -Leaf $MyInvocation.MyCommand.Path) -replace '\.Tests\.', '.'
. "$here\$sut"

Describe "ivc" {
    It "should be null when invoke from function." {
        $r = ivc-one-infunc
        $r | Should -BeNullOrEmpty
    }

    <#
    $here string are not safe.
    #>
    It "should not be null when invoke from top." {
        $r = ivc-one-fromtop
        $r | Split-Path -Leaf | Should -Be 'ivc.ps1'
    }

    It "should not be null when invoke from top nest." {
        $r = ivc-nested
        $r | Out-Host
        $r | Split-Path -Leaf | Should -Be 'ivc.1.ps1'
    }
}
