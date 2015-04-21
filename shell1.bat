goto flag1
@ls -l
:flag1
@dir
@rem ok rem
echo shell_1

for /L %%a in (0,1,255) do ping 192.168.0.%%a -n 1 >> tmp.txt