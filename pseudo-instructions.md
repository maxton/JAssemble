# Pseudoinstructions
JAssemble supports a number of pseudoinstructions to make writing assembly
programs easier. The following pseudoinstructions are currently supported by the
assembler; more instructions may be added in the future.

As these are not part of the official ISA, their *expansions* may change, but
their *functionality* should always remain the same.

## Overview of Supported Instructions
|Pseudoinstruction      |Result                                          |
|-----------------------|------------------------------------------------|
|`neg  $rd, $rs`        | `R[rd] = -R[rs]`                               |
|`sub  $rd, $rs, $rt`   | `R[rd] = R[rs] - R[rt]`                        |
|`subi $rs, $rd, IMM`   | `R[rd] = R[rs] - IMM`                          |
|`move $rs, $rd`        | `R[rd] = R[rs]`                                |
|`li $rd, IMM`          | `R[rd] = IMM`                                  |
|`j LABEL`              | `PC = PC + offset(LABEL)`                      |

### neg
`neg  $rd, $rs` 
#### Description
Negates the contends of register $rd.
#### Expansion
```
inv  $rd, $rs
addi $rd, $rd, 1
```


### sub
`sub  $rd, $rs, $rt` 
#### Description
Subtracts $rt from $rs and stores the result into $rd.
#### Expansion
```
inv  $rt
addi $rs, $rd, 1
```


### subi
`subi $rs, $rd, IMM`
#### Description
Substracts IMM from $rs and stores the result in $rd.

In this case, the assembler simply negates the immediate value and makes an addi
instruction.
#### Expansion
```
addi $rs, $rd, -(IMM)
```


### move
`move $rs, $rd`
#### Description
Sets the contents of $rd to the contents of $rs.
#### Expansion
```
or $rd, $rs, $rs
```


### li
`li $rd, IMM` 
#### Description
Loads the immediate value IMM into register $rd.
#### Expansion
```
clr $rd
ori $rd, $rd, IMM
```


### j
`j LABEL`
#### Description
Unconditionally jumps to LABEL.
#### Expansion
```
beq $0, $0, LABEL
```