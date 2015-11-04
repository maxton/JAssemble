# JAssemble
A simple 8-bit RISC assembler written in Java.

## Supported Instructions
|Name                   |Type |Desc                                                    |
|-----------------------|:---:|--------------------------------------------------------|
|`lw   $rs, $rd, ofs`   |  I  | `R[rd] = MEM[ofs + R[rs]]`                             |
|`sw   $rs, $rd, ofs`   |  I  | `MEM[ofs  + R[rs]] = R[rd]`                            |
|`add  $rd, $rs, $rt`   |  R  | `R[rd] = R[rs] + R[rt]`                                |
|`addi $rs, $rd, IMM`   |  I  | `R[rd] = R[rs] + IMM`                                  |
|`inv  $rd, $rs`        |  R  | `R[rd] = ~R[rs]`                                       |
|`and  $rd, $rs, $rt`   |  R  | `R[rd] = R[rs] & R[rt]`                                |
|`andi $rs, $rd, IMM`   |  I  | `R[rd] = R[rs] & IMM`                                  |
|`or   $rd, $rs, $rt`   |  R  | `R[rd] = R[rs] | R[rt]`                                |
|`ori  $rs, $rd, IMM`   |  I  |  `R[rd] = R[rs] | IMM`                                 |
|`sra  $rs, $rd, SHAMT` |  I  | `R[rd] = R[rs] >> SHAMT`                               |
|`sll  $rs, $rd, SHAMT` |  I  | `R[rd] = R[rs] << SHAMT`                               |
|`beq  $rs, $rd, LABEL` |  J  | `PC = (R[rs] == R[rd]) ? PC + offset(LABEL) : PC + 2`  |
|`bne  $rs, $rd, LABEL` |  J  | `PC = (R[rs] != R[rd]) ? PC + offset(LABEL) : PC + 2`  |
|`clr  $rd`             |  R  | `R[rd] = 0`                                            |
|`sub  $rd, $rs, $rt`   |  P  | `R[rd] = R[rs] - R[rt]`                                |
|`subi $rs, $rd, IMM`   |  P  | `R[rd] = R[rs] - IMM`                                  |
|`move $rs, $rd`        |  P  | `R[rd] = R[rs]`                                        |
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
assembly code and press "Assemble!".
![Running the Assembler](//i.imgur.com/uvT97nx.png)