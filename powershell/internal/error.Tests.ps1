
Describe "try catch throw" {
    it "should catch string error." {
        try {
            throw "abc"
        } catch {
            $error[0].TargetObject.GetType() | Out-Host
            $error[0].TargetObject | Should -BeOfType [string]
            $error[0].targetobject | Should -Be "abc"
        }
    }

    it "should catch int error." {
        try {
            throw 55
        } catch {
            $error[0].TargetObject.GetType() | Out-Host
            $error[0].TargetObject | Should -BeOfType [int]
            $error[0].targetobject | Should -Be 55
        }
    }

    it "should catch hashtable error." {
        try {
            throw @{a=1;b=2}
        } catch {
            $error[0].TargetObject.GetType() | Out-Host
            $error[0].TargetObject | Should -BeOfType [hashtable]
            $error[0].targetobject.a | Should -Be 1
            $error[0].targetobject.b | Should -Be 2
        }
    }

    it "should split all kind of path." {
         Split-Path -Parent "a\b\c" | Should -Be 'a\b'
         Split-Path -Parent "a\\b\\c" | Should -Be 'a\\b\'
         (Split-Path -Parent "a/b/c") -replace '\\','/' | Should -Be 'a/b'
    }
}