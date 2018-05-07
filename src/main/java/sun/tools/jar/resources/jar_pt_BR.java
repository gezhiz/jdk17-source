package sun.tools.jar.resources;

import java.util.ListResourceBundle;

public final class jar_pt_BR extends ListResourceBundle {
    protected final Object[][] getContents() {
        return new Object[][] {
            { "error.bad.cflag", "flag 'c' requer que os arquivos de manifesto ou entrada sejam especificados!" },
            { "error.bad.eflag", "o flag 'e' e manifesto com o atributo 'Main-Class' n\u00E3o podem ser especificados \njuntos!" },
            { "error.bad.option", "Uma das op\u00E7\u00F5es -{ctxu} deve ser especificada." },
            { "error.bad.uflag", "o flag 'u' requer que arquivos de manifesto, o flag 'e' ou arquivos de entrada sejam especificados!" },
            { "error.cant.open", "n\u00E3o \u00E9 poss\u00EDvel abrir: {0} " },
            { "error.create.dir", "{0} : n\u00E3o foi poss\u00EDvel criar o diret\u00F3rio" },
            { "error.illegal.option", "Op\u00E7\u00E3o inv\u00E1lida: {0}" },
            { "error.incorrect.length", "largura incorreta durante o processamento: {0}" },
            { "error.nosuch.fileordir", "{0} : n\u00E3o h\u00E1 tal arquivo ou diret\u00F3rio" },
            { "error.write.file", "Erro ao gravar o arquivo jar existente" },
            { "out.added.manifest", "manifesto adicionado" },
            { "out.adding", "adicionando: {0}" },
            { "out.create", "  criado: {0}" },
            { "out.deflated", "(vazio {0}%)" },
            { "out.extracted", "extra\u00EDdo: {0}" },
            { "out.ignore.entry", "ignorando entrada {0}" },
            { "out.inflated", " \\cheio: {0}" },
            { "out.size", "(dentro = {0}) (fora= {1})" },
            { "out.stored", "(armazenado 0%)" },
            { "out.update.manifest", "manifesto atualizado" },
            { "usage", "Uso: arquivos jar {ctxui}[vfm0Me] [jar-file] [manifest-file] [entry-point] [-C dir] ...\nOp\u00E7\u00F5es:\n    -c  cria novo arquivo compactado\n    -t  lista o sum\u00E1rio do arquivo compactado\n    -x  extrai arquivos com o nome (ou todos) do arquivo compactado\n    -u  atualizar o arquivo compactado existente\n    -v  gera sa\u00EDda detalhada na sa\u00EDda padr\u00E3o\n    -f  especifica o nome do arquivo do arquivo compactado\n    -m  inclui as informa\u00E7\u00F5es do manifesto do arquivo de manifesto especificado\n    -e  especifica o ponto de entrada da aplica\u00E7\u00E3o para aplica\u00E7\u00E3o independente \n        empacotando em um arquivo jar execut\u00E1vel\n    -0  armazena somente; n\u00E3o usa compacta\u00E7\u00E3o ZIP\n    -M  n\u00E3o cria um arquivo de manifesto para as entradas\n    -i  gera informa\u00E7\u00F5es de \u00EDndice para os arquivos especificados\n    -C  altera para o diret\u00F3rio e inclui o arquivo seguinte\nSe nenhum arquivo for um diret\u00F3rio, ent\u00E3o ser\u00E1 processado repetidamente.\nO nome do arquivo de manifesto, o nome do arquivo compactado e o nome do ponto de entrada s\u00E3o\nespecificados na mesma ordem dos flags  'm', 'f' e 'e'.\n\nExemplo 1: para arquivar dois arquivos de classe em um arquivo compactado com o nome classes.jar: \n       jar cvf classes.jar Foo.class Bar.class \nExemplo 2: use um arquivo de manifesto existente 'mymanifest' e arquive todos os\n           arquivos no diret\u00F3rio foo/ na 'classes.jar': \n       jar cvfm classes.jar mymanifest -C foo/ .\n" },
        };
    }
}
