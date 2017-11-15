package org.desenvolvimento.aberto;

import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;
 
/**
 * Servlet implementation class MeuServlet
 *
 * Classe do Mix
 */
@WebServlet("/Mix")
public class Mix extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    /**
     * @see HttpServlet#HttpServlet()
     *
     * Construtor do Servlet
     */
    public Mix() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    //num linhas , num colunas
    static int n, m, maxminform;																					
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     *
     * Método GET
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        String meuHtml = "<!DOCTYPE html>\r\n" + 
        		"<html lang=\"en\">\r\n" + 
        		"\r\n" + 
        		"  <head>\r\n" + 
        		"\r\n" + 
        		"    <meta charset=\"utf-8\">\r\n" + 
        		"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n" + 
        		"    <meta name=\"description\" content=\"\">\r\n" + 
        		"    <meta name=\"author\" content=\"\">\r\n" + 
        		"\r\n" + 
        		"    <title>TP2 Mix de Produção</title>\r\n" + 
        		"\r\n" + 
        		"    <!-- Bootstrap core CSS -->\r\n" + 
        		"    <link href=\"vendor/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\r\n" + 
        		"\r\n" + 
        		"    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\r\n" + 
        		"\r\n" + 
        		"    <!-- Custom styles for this template -->\r\n" + 
        		"\r\n" + 
        		"  </head>\r\n" + 
        		"\r\n" + 
        		"  <body>\r\n" + 
        		"\r\n" + 
        		"    <!-- Navigation -->\r\n" + 
        		"    <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark fixed-top\">\r\n" + 
        		"      <div class=\"container\">\r\n" + 
        		"        <a class=\"navbar-brand\" href=\"#\">TP2 Otimizacao</a>\r\n" + 
        		"        <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarResponsive\" aria-controls=\"navbarResponsive\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n" + 
        		"          <span class=\"navbar-toggler-icon\"></span>\r\n" + 
        		"        </button>\r\n" + 
        		"        <div class=\"collapse navbar-collapse\" id=\"navbarResponsive\">\r\n" + 
        		"          <ul class=\"navbar-nav ml-auto\">\r\n" + 
        		"            <li class=\"nav-item active\">\r\n" + 
        		"              <a class=\"nav-link\" href=\"#\">Inicio\r\n" + 
        		"                <span class=\"sr-only\">(current)</span>\r\n" + 
        		"              </a>\r\n" + 
        		"            </li>\r\n" + 
        		"          </ul>\r\n" + 
        		"        </div>\r\n" + 
        		"      </div>\r\n" + 
        		"    </nav>\r\n" + 
        		"\r\n" + 
        		"    <!-- Page Content -->\r\n" + 
        		"    <div class=\"container\">\r\n" + 
        		"      <div class=\"row\">\r\n" + 
        		"        <div class=\"col-lg-12 text-center\">\r\n" + 
        		"          \r\n" + 
        		"          <h1 class=\"mt-5\">Mix de Produção</h1>\r\n" + 
        		"          <p class=\"lead\">Preencha as informacoes abaixo para que possamos executar o GLPK e mostrar o resultado de sua funcao objetiva!</p>\r\n" +
        		"		   <p><b>POR FAVOR, UTILIZE \'.\' PARA NUMEROS REAIS.</b></p>\r\n" +
        		"\r\n" + 
        		"          <hr>\r\n" + 
        		"          \r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <span>\r\n" + 
        		"              <input type=\"radio\" name=\"tipo\" id=\"MAX\" value=\"MAX\" checked=\"checked\"> Maximizacao\r\n" + 
        		"              \r\n" + 
        		"              <input type=\"radio\" name=\"tipo\" id=\"MIN\" value=\"MIN\"> Minimizacao\r\n" + 
        		"            </span>\r\n" + 
        		"          </div>          \r\n" + 
        		"          \r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <!-- numero de linhas -->\r\n" + 
        		"            <span>\r\n" + 
        		"              Informe o numero de restricoes:\r\n" + 
        		"            </span>\r\n" + 
        		"            <input id=\"linha\" type=\"number\" min=\"0\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <!-- numero de Colunas -->\r\n" + 
        		"            <span>\r\n" + 
        		"              Informe o numero de variaveis:\r\n" + 
        		"            </span>\r\n" + 
        		"            <input id=\"coluna\" type=\"number\" min=\"0\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"			 <p>Passo 1:</p>	"	+
        		"          </div>\r\n" +
        		"          <div class=\"row\">\r\n" +
        		"            <input type=\"button\" value=\"Gerar Tabela\" onClick=\"addTable()\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <table id=\"myTable\">\r\n" + 
        		"            </table>\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"			 <p>Passo 2 (Após tabela já preenchida):</p>	"	+
        		"          </div>\r\n" + 
        		"          <div class=\"row\">\r\n" +
        		"            <input id=\"btnCalcular\" name=\"btnCalcular\" type=\"button\" value=\"Calcular\" onClick=\"calcular()\">\r\n" + 
        		"          </div>\r\n" + 
        		"\r\n" + 
        		"          <div class=\"row\">\r\n" + 
        		"            <table id=\"tableFinal\">\r\n" + 
        		"            </table>\r\n" + 
        		"          </div>\r\n" +
        		"\r\n" +         	
        		"		  <form class=\"invisible\" id=\"formulario\" method=\"post\" action=\"Mix\">\r\n" + 
        		"				<input type=\"text\" id=\"qtdLinha\" name=\"qtdLinha\">\r\n" + 
        		"				<input type=\"text\" id=\"qtdColuna\" name=\"qtdColuna\">\r\n" + 
        		"				<input type=\"text\" id=\"vetor\" name=\"vetor\">\r\n" +
        		"				<input type=\"text\" id=\"minform\" name=\"minform\">\r\n" + 
        		"				<input type=\"text\" id=\"teto\" name=\"teto\">\r\n" + 
        		"		  </form>" +
        		"        </div>\r\n" + 
        		"      </div>\r\n" + 
        		"    </div>\r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"    <!-- Bootstrap core JavaScript -->\r\n" + 
        		"    <script src=\"vendor/jquery/jquery.min.js\"></script>\r\n" + 
        		"    <script src=\"vendor/popper/popper.min.js\"></script>\r\n" + 
        		"    <script src=\"vendor/bootstrap/js/bootstrap.min.js\"></script>\r\n" + 
        		"    <script type=\"text/javascript\">\r\n" + 
        		"      function addTable() {\r\n" + 
        		"          var table = document.getElementById(\"myTable\");\r\n" + 
        		"          //pega o valor digitado e coloca na variavel de linha\r\n" + 
        		"          var qtdLinha = document.getElementById(\"linha\").value;\r\n" + 
        		"          //pega o valor digitado e coloca na variavel de coluna\r\n" + 
        		"          var qtdColuna = document.getElementById(\"coluna\").value;\r\n" + 
        		"\r\n" + 
        		"          var variavelExcFolga = parseFloat(qtdColuna)+1;\r\n" + 
        		"		  \r\n" + 
        		"		      var row = table.insertRow();\r\n" + 
        		"		  \r\n" + 
        		"    		  row.innerHTML = \"<th>FO ->\" + \r\n" + 
        		"    						   \"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Z=</th>\";\r\n" + 
        		"    		  for(var i = 0; i < qtdColuna; i++){						 \r\n" + 
        		"    			var cell = row.insertCell();\r\n" + 
        		"    			cell.innerHTML = \"x\" + (i+1) + \": &nbsp;<input class='variavelFO' size='1' value='0'>\";\r\n" + 
        		"    		  }		\r\n" + 
        		"    		  \r\n" + 
        		"              for (var i=0; i<qtdLinha; i++) {\r\n" + 
        		"                //insere cada linha\r\n" + 
        		"                var row = table.insertRow();\r\n" + 
        		"                row.innerHTML = \"<th>Restricao\" + (i+1) + \"-></th>\";\r\n" + 
        		"                \r\n" + 
        		"                //var cell = row.insertCell();\r\n" + 
        		"\r\n" + 
        		"                //insere colune ML\r\n" + 
        		"                //cell.innerHTML = \"ML: &nbsp;<input class='ml'size='1' value='0'> &nbsp;&nbsp;\";\r\n" + 
        		"                for(var j=0; j<qtdColuna; j++){            \r\n" + 
        		"                  //insere cada coluna na linha atual\r\n" + 
        		"                  var cell = row.insertCell();                               \r\n" + 
        		"                  cell.innerHTML = \"x\" + (j+1) + \": &nbsp;<input class='variaveis' size='1' value='0'>\";\r\n" + 
        		"                };\r\n" + 
        		"                //insere o ML\r\n" + 
        		"                var cell = row.insertCell();            \r\n" + 
        		"                //cell.innerHTML = \"x\" + variavelExcFolga + \":&nbsp; <input class='excessofolga' size='1' value='0' >\";			\r\n" + 
        		"    			      cell.innerHTML = \"<select class='operador'> \" +\r\n" + 
        		"    								\"<option value='maiorigual' selected='selected'>>=</option>\" +\r\n" + 
        		"    								\"<option value='menorigual'><=</option>\" + \r\n" + 
        		"    							 \"</select>\" +\r\n" + 
        		"    							 \"&nbsp; <input class='excessofolga' size='1' value='0' >\";       							 \r\n" + 
        		"                variavelExcFolga++;\r\n" + 
        		"              };\r\n" + 
        		"              \r\n" + 
        		"              //cria id unico pra cada input de variavel FO\r\n" + 
        		"              var fo=0;\r\n" + 
        		"              $('.variavelFO').each(function(){\r\n" + 
        		"                  fo++;\r\n" + 
        		"                  var newID='fo'+fo;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"              });  \r\n" + 
        		"              //cria id unico pra cada input de variavel\r\n" + 
        		"              var x=0;\r\n" + 
        		"              $('.variaveis').each(function(){\r\n" + 
        		"                  x++;\r\n" + 
        		"                  var newID='x'+x;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"              });         \r\n" + 
        		"              //cria id unico pra cada input de excesso ou folga\r\n" + 
        		"              var ef=variavelExcFolga-qtdLinha;\r\n" + 
        		"              $('.excessofolga').each(function(){              \r\n" + 
        		"                  var newID='ef'+ef;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"                  ef++;\r\n" + 
        		"              });\r\n" + 
        		"    		      //cria id unico pra cada combobox de operador\r\n" + 
        		"              var op=0;\r\n" + 
        		"              $('.operador').each(function(){\r\n" + 
        		"                  op++;\r\n" + 
        		"                  var newID='op'+op;\r\n" + 
        		"                  $(this).attr('id',newID);\r\n" + 
        		"              });\r\n" + 
        		"//atributos name\r\n" + 
        		"			  //cria id unico pra cada input de variavel FO\r\n" + 
        		"              var fo=0;\r\n" + 
        		"              $('.variavelFO').each(function(){\r\n" + 
        		"                  fo++;\r\n" + 
        		"                  var newID='fo'+fo;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"              });  \r\n" + 
        		"              //cria id unico pra cada input de variavel\r\n" + 
        		"              var x=0;\r\n" + 
        		"              $('.variaveis').each(function(){\r\n" + 
        		"                  x++;\r\n" + 
        		"                  var newID='x'+x;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"              });         \r\n" + 
        		"              //cria id unico pra cada input de excesso ou folga\r\n" + 
        		"              var ef=variavelExcFolga-qtdLinha;\r\n" + 
        		"              $('.excessofolga').each(function(){              \r\n" + 
        		"                  var newID='ef'+ef;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"                  ef++;\r\n" + 
        		"              });\r\n" + 
        		"    		      //cria id unico pra cada combobox de operador\r\n" + 
        		"              var op=0;\r\n" + 
        		"              $('.operador').each(function(){\r\n" + 
        		"                  op++;\r\n" + 
        		"                  var newID='op'+op;\r\n" + 
        		"                  $(this).attr('name',newID);\r\n" + 
        		"              });\r\n"+
        		"      };\r\n" + 
        		"\r\n" + 
        		"      function calcular() {\r\n" + 
        		"        var table2 = document.getElementById(\"tableFinal\");\r\n" + 
        		"        //pega o valor digitado e coloca na variavel de linha\r\n" + 
        		"        var qtdLinha = document.getElementById(\"linha\").value;\r\n" + 
        		"        //pega o valor digitado e coloca na variavel de coluna\r\n" + 
        		"        var qtdColuna = document.getElementById(\"coluna\").value;\r\n" +
        		"        //imprime n e m\r\n" + 
        		"        var n = parseFloat(qtdLinha)+1;\r\n" + 
        		"        var m = parseFloat(qtdColuna)+1;\r\n" + 
        		"		 var arrayMaiorMenor = [n-1];\r\n"+
        		"		 for(var i=1; i<n; i++){\r\n"+
        		"			console.log(document.getElementById(\"op\"+i).value);\r\n"+
        		"		 	if(document.getElementById(\"op\"+i).value == \"maiorigual\") {\r\n"+
        		"				arrayMaiorMenor[i-1] = 1;\r\n"+
        		"				console.log(\">=\");\r\n"+
        		"		 	}else{\r\n" +
        		"				arrayMaiorMenor[i-1] = 0;\r\n"+
        		"				console.log(\"<=\");\r\n"+
        		" 			}\r\n" +
        		"		 }\r\n"+
        		"		console.log(arrayMaiorMenor);\r\n"+
        		"		document.getElementById(\"teto\").value = arrayMaiorMenor;" +
        		"\r\n" + 
        		"        var row = table2.insertRow();\r\n" + 
        		"        //document.write(\"n=\"+n+\";m=\"+m+\";\");\r\n" + 
        		"        var cell = row.insertCell();\r\n" + 
        		"        cell.innerHTML = \"n=\"+n+\";m=\"+m+\";\";\r\n" + 
        		"\r\n" + 
        		"        var bla = new Array(n);\r\n" + 
        		"        var testee = 1;\r\n" + 
        		"        for (var i = 0; i < n; i++) {\r\n" + 
        		"          bla[i] = new Array(m);\r\n" + 
        		"        }\r\n" + 
        		"\r\n" + 
        		"        //variaveis para montar o array final\r\n" + 
        		"        var linhaa = 0;\r\n" + 
        		"        var colunaa = 0;\r\n" + 
        		"\r\n" + 
        		"        //montando a matriz\r\n" + 
        		"        //se escolher MAX\r\n" + 
        		"        if(document.getElementById('MAX').checked){\r\n" + 
        		"          //FO\r\n" + 
        		"          var row = table2.insertRow();\r\n" + 
        		"          for (var i=0; i<m; i++) {\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            if(i==0){\r\n" + 
        		"              var num = parseInt(0);\r\n" + 
        		"              //daria um insere detro [0][0]\r\n" + 
        		"              bla[linhaa][i] = 0;\r\n" + 
        		"            }else{\r\n" + 
        		"              var num = parseFloat(document.getElementById('fo'+i).value);\r\n" + 
        		"              bla[linhaa][i] = num;\r\n" + 
        		"            }\r\n" + 
        		"            cell.innerHTML = num;\r\n" + 
        		"          };\r\n" + 
        		"\r\n" + 
        		"          linhaa++;\r\n" + 
        		"          colunaa=0;\r\n" + 
        		"\r\n" + 
        		"          //restricoes\r\n" + 
        		"          var eefe = parseFloat(qtdColuna)+1;\r\n" + 
        		"          var xis = 1;\r\n" + 
        		"		   var maxmin;"+
        		"          for (var i=0; i<qtdLinha; i++) {\r\n" + 
        		"            var row = table2.insertRow();\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            //se for >= troca o sinal do ML\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"maiorigual\"){\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"              //se for <= nao troca o sinal\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            eefe++;\r\n" + 
        		"\r\n" + 
        		"            //se for >= troca o sinal da linha\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"maiorigual\"){\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] =num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              }\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"            //se for <= nao troca o sinal\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] =num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              }\r\n" + 
        		"            }\r\n" + 
        		"            linhaa++;\r\n" + 
        		"            colunaa=0;\r\n" + 
        		"          }\r\n" + 
        		"          console.log(\"MAX\");\r\n" + 
        		"		   document.getElementById(\"minform\").value = \"max\";"+
        		"          console.log(bla);\r\n" + 
        		"        }\r\n" + 
        		"        else{\r\n" + 
        		"          //se escolher MIN\r\n" + 
        		"          //FO\r\n" + 
        		"          var row = table2.insertRow();\r\n" + 
        		"          for (var i=0; i<m; i++) {\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            if(i==0){\r\n" + 
        		"              var num = parseFloat(0);\r\n" + 
        		"            }else{\r\n" + 
        		"              var num = parseFloat(document.getElementById('fo'+i).value);\r\n" + 
        		"            }\r\n" + 
        		"            //multiplicar por -1\r\n" + 
        		"            num = parseFloat(num);\r\n" + 
        		"            cell.innerHTML = num;\r\n" + 
        		"            bla[linhaa][i] = num;\r\n" + 
        		"          };\r\n" + 
        		"\r\n" + 
        		"          linhaa++;\r\n" + 
        		"          colunaa = 0;\r\n" + 
        		"\r\n" + 
        		"          //restricoes\r\n" + 
        		"          var eefe = parseFloat(qtdColuna)+1;\r\n" + 
        		"          var xis = 1;\r\n" + 
        		"          for (var i=0; i<qtdLinha; i++) {\r\n" + 
        		"            var row = table2.insertRow();\r\n" + 
        		"            var cell = row.insertCell();\r\n" + 
        		"            //se for >= troca o sinal do ML 2x (continua o mesmo)\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"menorigual\"){\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"            //se for <= troca o sinal 1x\r\n" + 
        		"              num = parseFloat(document.getElementById(\"ef\"+eefe).value);\r\n" + 
        		"              cell.innerHTML = num;\r\n" + 
        		"              bla[linhaa][colunaa] = num;\r\n" + 
        		"              colunaa++;\r\n" + 
        		"            }\r\n" + 
        		"            eefe++;\r\n" + 
        		"\r\n" + 
        		"            if((document.getElementById(\"op\"+(i+1)).value) == \"menorigual\"){\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] = num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              };\r\n" + 
        		"            }\r\n" + 
        		"            else{\r\n" + 
        		"              for(var j=0; j<qtdColuna; j++){\r\n" + 
        		"                var cell = row.insertCell();\r\n" + 
        		"                //multiplicar por -1\r\n" + 
        		"                num = parseFloat(document.getElementById(\"x\"+xis).value);\r\n" + 
        		"                cell.innerHTML = num;\r\n" + 
        		"                bla[linhaa][colunaa] = num;\r\n" + 
        		"                colunaa++;\r\n" + 
        		"                xis++;\r\n" + 
        		"              }\r\n" + 
        		"            }\r\n" + 
        		"            linhaa++;\r\n" + 
        		"            colunaa=0;\r\n" + 
        		"          }\r\n" + 
        		"          console.log(\"MIN\");\r\n" + 
        		"		   document.getElementById(\"minform\").value = \"min\";"+
        		"          console.log(bla);\r\n" + 
        		"        }//fim else\r\n" + 
        		"\r\n" + 
        		"		document.getElementById(\"vetor\").value = bla;	" +
        		"		document.getElementById(\"qtdLinha\").value = n;	" +
        		"		document.getElementById(\"qtdColuna\").value = m;	" +
        		"		}\r\n" + 
        		"\r\n" + 
        		"		document.getElementById(\"btnCalcular\").addEventListener(\"click\", function(){\r\n" + 
        		"			document.getElementById(\"formulario\").submit();\r\n" + 
        		"		});" +
        		"    </script>\r\n" + 
        		"  </body>\r\n" + 
        		"</html>";
 
        PrintWriter imprimir = response.getWriter();
 
        imprimir.println(meuHtml);      
 
        // Recupera Parametros da URL
 
        String parametro = request.getParameter("Parametro1");  
 
        if (parametro != null )
        {
            imprimir.println("O parametro digitado foi: " + parametro);
        }
        
        doPost(request, response);
 
    }
 
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     *
     * Método POST
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String qtdLinha = request.getParameter("qtdLinha");
    	String qtdColuna = request.getParameter("qtdColuna");
    	String vetor = request.getParameter("vetor");
    	String minmaxform = request.getParameter("minform");
    	String tetoform = request.getParameter("teto");
    	response.setContentType("text/html");
    	
		PrintWriter out = response.getWriter();
		
		n = Integer.parseInt(qtdLinha);
		m = Integer.parseInt(qtdColuna);
		
		double[][] s = new double[n][m];
		String[] itensVetor = vetor.split(",");
		int cont = 0;
		for(int o=0; o<n; o++){
        	for(int p=0; p<m; p++){
        		if(cont < itensVetor.length) {
        			s[o][p] = Double.parseDouble(itensVetor[cont]);
            		cont++;
        		}//fim if
        	}//fim for
        }//fim for
		
		n = n-1;
		m = m-1;
		
		int[]t = new int[n];
		String[] itensTeto = tetoform.split(",");
		for(int o=0; o<n; o++){
			t[o] = Integer.parseInt(itensTeto[o]);
        }//fim for
		//menor é 0
		//maior é 1
//--------------------------------------------------------------------------------------------------------------------- começa o exemplo
		glp_prob lp;
		glp_smcp parm;
    	SWIGTYPE_p_int ind;
    	SWIGTYPE_p_double val;
    	int ret;
		
		//Vars
		int numVar, numRes;
		boolean MIN;
		if(minmaxform.equals("max")) {
			MIN = false;
		}else {
			MIN = true;
		}
		double coef = 0;				//<--
		
		try {			
			// Create problem
    		lp = GLPK.glp_create_prob();
    		out.println("Problema criado");
    		out.println(" ");
			GLPK.glp_set_prob_name(lp, "Problem1");
			
			// Define columns
			numVar = m;
    		GLPK.glp_add_cols(lp, numVar);
			for(int i =1; i<numVar+1; i++){
    			GLPK.glp_set_col_name(lp, i, "x"+i);
    			GLPK.glp_set_col_kind(lp, i, GLPKConstants.GLP_CV);
    			GLPK.glp_set_col_bnds(lp, i, GLPKConstants.GLP_LO, 0, 0);
			}
		
			// Create constraints

			// Allocate memory
       		ind = GLPK.new_intArray(numVar+1);
    		val = GLPK.new_doubleArray(numVar+1);
			
    		// Create rows
    		numRes = n;					
			GLPK.glp_add_rows(lp, numRes+1);
			
			// Set row details
    		for(int i =1; i<numRes+1; i++){
    			
				GLPK.glp_set_row_name(lp, i, "c"+i);
				
				//SET_ROW_BNDS
				if(t[i-1] != 0){
					//colocar os excesso folga em ef
					coef = s[i][0];
					GLPK.glp_set_row_bnds(lp, i, GLPKConstants.GLP_LO, coef, 0);
    			}else{
					coef = s[i][0];
					GLPK.glp_set_row_bnds(lp, i, GLPKConstants.GLP_UP, 0, coef);
				}//fim if >= ou <= de cada restrição
				
				//SET_ITEM
				for(int j =1; j<numVar+1; j++){
					coef = s[i][j];
					GLPK.intArray_setitem(ind, j, j);
    				GLPK.doubleArray_setitem(val, j, coef);
				}//fim for dos coeficientes de cada restricao
				GLPK.glp_set_mat_row(lp, i, numVar, ind, val);
			}
			
			// Free memory
    		GLPK.delete_intArray(ind);
			GLPK.delete_doubleArray(val);
			
			// Define objective
    		GLPK.glp_set_obj_name(lp, "z");
			if(MIN){
				GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);
    		}else{
				GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
			}//fim if de MIN ou MAX
			
			for(int i =1; i<numVar+1; i++){
				coef = s[0][i];
    			GLPK.glp_set_obj_coef(lp, i, coef);
			}//fim for de z
			
			// Solve model
    		parm = new glp_smcp();
    		GLPK.glp_init_smcp(parm);
    		ret = GLPK.glp_simplex(lp, parm);
    		GLPK.glp_term_out(1);
    		
    		// Retrieve solution
    		if (ret == 0){
    			write_lp_solution(lp, response);
    		}else{
       		 	out.println("O problema não pode ser resolvido");
    		}

       		// Free memory
			GLPK.glp_delete_prob(lp);
		}
		catch(Exception ex) {
			ex.printStackTrace();			
		}
				
		out.close();
    }  
    public static void write_lp_solution(glp_prob lp, HttpServletResponse response) throws ServletException, IOException {
		int quant;
		String nameWrite;
		double valWrite;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		nameWrite = GLPK.glp_get_obj_name(lp);
		valWrite = GLPK.glp_get_obj_val(lp);
		out.print(nameWrite);
		out.print(" = ");
		out.println(valWrite);
		quant = GLPK.glp_get_num_cols(lp);
	    
		for (int i = 1; i <= quant; i++) {
			nameWrite = GLPK.glp_get_col_name(lp, i);
   			valWrite = GLPK.glp_get_col_prim(lp, i);
			out.print(nameWrite);
			out.print(" = ");
			out.println(valWrite);
		}
	}
}