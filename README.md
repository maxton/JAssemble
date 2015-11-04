# JAssemble
A simple 8-bit RISC assembler written in Java.

## Supported Instructions
|Name                   | Type |  Desc                    |
|-----------------------|:---:|-----------------------------|
|`lw $rs, $rd, ofs`     | I | `R[rd] = MEM[ofs + R[rs]]`  |
|`sw $rs, $rd, ofs`     | I | `MEM[ofs  + R[rs]] = R[rd]` |
|`add $rd, $rs, $rt`    | R | `R[rd] = R[rs] + R[rt]`     |
|`addi $rs, $rd, IMM`   | I | `R[rd] = R[rs] + IMM`       |
|`inv $rd, $rs`         | R | `R[rd] = ~R[rs]`            |
|`and $rd, $rs, $rt`    | R | `R[rd] = R[rs] & R[rt]`     |
|`andi $rs, $rd, IMM`   | I | `R[rd] = R[rs] & IMM`       |
|`or $rd, $rs, $rt`     | R | `R[rd] = R[rs] | R[rt]`     |
|`ori $rs, $rd, IMM`    | I | `R[rd] = R[rs] | IMM`       |
|`sra $rs, $rd, SHAMT`  | I | `R[rd] = R[rs] >> SHAMT`    |
|`sll $rs, $rd, SHAMT`  | I | `R[rd] = R[rs] << SHAMT`    |
|`beq $rs, $rd, LABEL`  | J | `PC = (R[rs] == R[rd]) ? PC + offset(LABEL) : PC + 4`  |
|`bne $rs, $rd, LABEL`  | J | `PC = (R[rs] != R[rd]) ? PC + offset(LABEL) : PC + 4`  |
|`clr $rd`              | R | `R[rd] = 0`                 |

