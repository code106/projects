Add-Type -TypeDefinition @"
    using System;
    using System.Runtime.InteropServices;
    public class Mouse {
        [DllImport("user32.dll")]
        public static extern void SetCursorPos(int x, int y);
    }
"@

while ($true) {
    $pos = [System.Windows.Forms.Cursor]::Position
    [Mouse]::SetCursorPos($pos.X + 1, $pos.Y)  # Moves cursor slightly
    Start-Sleep -Seconds 60
    [Mouse]::SetCursorPos($pos.X, $pos.Y)      # Moves it back
    Start-Sleep -Seconds 60
}
