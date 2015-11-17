# JAssemble
A simple 8-bit RISC assembler written in Java.

## Supported Instructions
|Name                   |Type |Desc                                                    |
|-----------------------|:---:|--------------------------------------------------------|
|`lw   $rd, ofs($rs)`   |  I  | `R[rd] = MEM[ofs + R[rs]]`                             |
|`sw   $rd, ofs($rs)`   |  I  | `MEM[ofs  + R[rs]] = R[rd]`                            |
|`add  $rd, $rs, $rt`   |  R  | `R[rd] = R[rs] + R[rt]`                                |
|`addi $rd, $rs, IMM`   |  I  | `R[rd] = R[rs] + IMM`                                  |
|`inv  $rd, $rt`        |  R  | `R[rd] = ~R[rt]`                                       |
|`and  $rd, $rs, $rt`   |  R  | `R[rd] = R[rs] & R[rt]`                                |
|`andi $rd, $rs, IMM`   |  I  | `R[rd] = R[rs] & IMM`                                  |
|`or   $rd, $rs, $rt`   |  R  | `R[rd] = R[rs] | R[rt]`                                |
|`ori  $rd, $rs, IMM`   |  I  | `R[rd] = R[rs] | IMM`                                  |
|`sra  $rd, $rs, SHAMT` |  I  | `R[rd] = R[rs] >> SHAMT`                               |
|`sll  $rd, $rs, SHAMT` |  I  | `R[rd] = R[rs] << SHAMT`                               |
|`beq  $rs, $rt, LABEL` |  J  | `PC = (R[rs] == R[rt]) ? PC + offset(LABEL) : PC + 2`  |
|`bne  $rs, $rt, LABEL` |  J  | `PC = (R[rs] != R[rt]) ? PC + offset(LABEL) : PC + 2`  |
|`clr  $rd`             |  R  | `R[rd] = 0`                                            |
|`neg  $rd, $rs`        |  P  | `R[rd] = -R[rs]`                                       |
|`sub  $rd, $rs, $rt`   |  P  | `R[rd] = R[rs] - R[rt]`                                |
|`subi $rd, $rs, IMM`   |  P  | `R[rd] = R[rs] - IMM`                                  |
|`move $rd, $rs`        |  P  | `R[rd] = R[rs]`                                        |
|`li $rd, IMM`          |  P  | `R[rd] = IMM`                                          |
|`j LABEL`              |  P  | `PC = PC + offset(LABEL)`                              |

### Example of supported assembly code:

```
main: li $0, 9
      li $1, 0
      clr $2
loop: subi $0, $0, 1
      addi $2, $2, 1
      beq $0, $1, end
      j loop
end:
      inv $2, $2
```

Output for the above code:
```
memory_initialization_radix=16;
memory_initialization_vector=d000,8009,d140,8500,d280,30ff,3a01,b102,b0fd,4880;
```

### Example of useful error messages:
```
mov $0, $2
addi $1, $2, $3
add $0, $2, $4
sub $0, $2, 3
```

Will show as errors:

```
Error: at line 1,
 Invalid instruction encountered: mov
Error: at line 2,
 For input string: "$3"
Error: at line 3,
 Register $4 is invalid.
Error: at line 4,
 Expected register, found 3
Couldn't assemble code:
 One or more instructions were malformed.
```

## Usage
JAssembler is mainly a GUI application. Simply type in or load a piece of
assembly code and press "Assemble!" to assemble the code to machine code,
or press "Simulate!" to test your program in the built-in simulator.
![Running the Assembler](https://i.imgur.com/rz3tYrV.png)
![Running the Simulator](https://i.imgur.com/BNj7wUL.png)