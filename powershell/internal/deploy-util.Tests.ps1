$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$psDir = $here | Split-Path -Parent
$sut = "${here}\deploy-util.ps1"
. "$sut"
. "$here\SshInvoker.ps1"
. "$here\deploy-config.ps1"
$ConfigFile = "${psDir}\deploy.json"
$dconfig = Get-DeployConfig -ConfigFile $ConfigFile

Describe "test backup aspect." {
    $workingDir = Join-Path $TestDrive "folder\working"
    New-Item -ItemType Directory -Path $workingDir

    if (-not (Test-Path -Path $workingDir)) {
        throw "create fixture directory failed. $workingDir"
    }
    $kkvFileInSrcFolder = Join-Path -Path $workingDir -ChildPath "kkv.txt"
    "abc" | Out-File $kkvFileInSrcFolder
    $parent = Split-Path -Path $workingDir -Parent

    It "should get max-backup is himself." {
        $tf = Get-MaxBackup -Path $workingDir
        $created = Join-Path $parent -ChildPath 'working'
        $tf |Should -Be $created
    }

    It "should get max-backup." {
        $workingDir1 = Join-Path $TestDrive "folder\working1"
        New-Item -ItemType Directory -Path $workingDir1
        $tf = Get-MaxBackup -Path $workingDir
        $created = Join-Path $parent -ChildPath 'working.1'
        $tf |Should -Be $created
    }
}

Describe "test remote backup aspect." {
    $workingDir = Join-Path $TestDrive "folder\working"
    New-Item -ItemType Directory -Path $workingDir

    if (-not (Test-Path -Path $workingDir)) {
        throw "create fixture directory failed. $workingDir"
    }
    $kkvFileInSrcFolder = Join-Path -Path $workingDir -ChildPath "kkv.txt"
    "abc" | Out-File $kkvFileInSrcFolder

    $remoteFolderNoEndSlash = "/tmp/folder"

    it "should backup remote folder." {
        $sshInvoker = Get-SshInvoker -dconfig $dconfig
        $rc = "rm -rf {0};mkdir -p {1}" -f $remoteFolderNoEndSlash, $remoteFolderNoEndSlash
        $sshInvoker.invoke($rc)
        $sshInvoker.scp($workingDir, $remoteFolderNoEndSlash, $true) # create /tmp/folder/working directory.
        $rworking = "$remoteFolderNoEndSlash/working"
        $lines = ,"Backup-LocalDirectory -Path ${rworking}"
        $rf = New-RemoteScriptFile -sshInvoker $sshInvoker -scriptLines $lines -DeployConfig $dconfig
        $sshInvoker.Invoke("pwsh -f $rf") | Should -Be '/tmp/folder/working.1'
        $sshInvoker.isFileExists("${rworking}.1") | Should -BeTrue
    }

    it "should create temporary script file." {
        $sshInvoker = Get-SshInvoker -dconfig $dconfig
        $lines = ,'echo hello'
        $rf =   New-RemoteScriptFile -sshInvoker $sshInvoker -scriptLines $lines -DeployConfig $dconfig
        $rf | Should -Be ($dconfig.scriptDir + "/running.ps1")
        $sshInvoker.isFileExists($rf) | Should -BeTrue
        $sshInvoker.Invoke("pwsh -f $rf") | Should -Be 'hello'
    }
}

Describe "deploy-util" {
    $workingDir = Join-Path $TestDrive "folder\working"
    New-Item -ItemType Directory -Path $workingDir

    if (-not (Test-Path -Path $workingDir)) {
        throw "create fixture directory failed. $workingDir"
    }
    $kkvFileInSrcFolder = Join-Path -Path $workingDir -ChildPath "kkv.txt"
    "abc" | Out-File $kkvFileInSrcFolder
    $parent = Split-Path -Path $workingDir -Parent

    It "should get max-backup not exists" {
        $tf = Get-MaxBackup -Path (Join-Path -Path $workingDir -ChildPath "notexists")
        $tf |Should -Be (Join-Path -Path $workingDir -ChildPath "notexists")
    }

    It "should back up in local" {
        $tf = Backup-LocalDirectory -Path $workingDir
        $created = Join-Path $parent -ChildPath 'working.1'
        $tf |Should -Be $created
    }

    It "should back up in local not exists throw exception." {
        { Backup-LocalDirectory -Path "${workingDir}notexists"} | Should -Throw "does'nt exists."
    }



    It "should get right here-string" {
        $v = 55
        $s = @"
abc
"@
        $s | Should -Be 'abc'

        $s = @"
 abc 
"@
        $s | Should -Be ' abc '

        $s = @"
 abc 

"@
        $s | Should -Be " abc `r`n"

        $s = @"
abc$v
"@
        $s | Should -Be 'abc55'
    }
}
