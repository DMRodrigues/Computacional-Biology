# OBRIGATORIO ESTAR NA SEGUINTE FORMA OS FICHEIROS
# Pasta (ex: Desktop)
#   |
#   |-> makefile
#   |
#   \->exercise
#        |
#        |-> Simulator.java
#        \-> Graphics.java



# define compiler and compiler flag variables
# the -g flag compiles with debugging information
JFLAGS = -g
JCC = javac
JCR = java

# estas duas variaveis encontram-se ligadas por causa
# primeiro do compilador
# segundo do java runtime environment
FOLDER = exercise/
PACKAGE = exercise.

# simbolic link para o codigo
SIMULATOR = $(FOLDER)Simulator.java
GRAPHICS = $(FOLDER)Graphics.java


# compilar o codigo e executar logo de seguida o simulador
default: simulator exec


# compilar o codigo
simulator: $(SIMULATOR) $(GRAPHICS)
	$(JCC) $(JFLAGS) $(SIMULATOR) $(GRAPHICS)


# executar o codigo ja compilado
# WARNING => EXECUTA O SIMULADOR <= WARNING
exec:
	($(JCR) $(PACKAGE)Graphics &) >/dev/null 2>&1


# remover o codigo compilado (aka .class)
clean:
	$(RM) $(FOLDER)*.class
	@echo "Tudo limpo!"
