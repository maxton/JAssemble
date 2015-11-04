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
main: clr $0
      addi $1, $0, 9
loop: addi $0, $0, 1
      beq $0, $1, end
      j loop
end:
      inv $0, $1
```

Output for the above code:
```
memory_initialization_radix=16;
memory_initialization_vector=d000,3409,3001,b102,b0fe,4400;
```
