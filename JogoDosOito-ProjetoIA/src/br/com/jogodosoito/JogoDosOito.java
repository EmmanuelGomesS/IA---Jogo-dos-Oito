package br.com.jogodosoito;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class JogoDosOito {

	private JFrame frmPuzzle;
	private JButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;
	static int[] solucao = {1,2,3,4,5,6,7,8,0};
	static int CIMA = 1;
	static int BAIXO = 2;
	static int DIREITA = 3;
	static int ESQUERDA = 4;

	static boolean printOn = true;

	static int tamanho_fila = 0;
	static int tamanho_fila_max = 0;

	static double custo_solucao = 0.0;

	static int nos_expandidos = 0;

	static int dimensao = 3;


	static int[] inicio = new int[] { 0, 2, 4, 7, 6, 3, 5, 8, 1 };

	static PriorityQueue fila ;
	static Set processados ;

	
	public JogoDosOito() {
		initialize();
	}

	
	private void initialize() {
		frmPuzzle = new JFrame();
		frmPuzzle.setIconImage(Toolkit.getDefaultToolkit().getImage(JogoDosOito.class.getResource("/br/com/jogodosoito/imagem/icon8.png")));
		frmPuzzle.setTitle("Jogo Dos 8 Números");
		frmPuzzle.setBounds(100, 100, 629, 506);
		frmPuzzle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPuzzle.setResizable(false);//removendo o botão maximizar
		frmPuzzle.setLocationRelativeTo(null);//alinhando ao centro da tela
		frmPuzzle.getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 44, 21);
		frmPuzzle.getContentPane().add(menuBar);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		JMenuItem mntmEmbaralharTabuleiro = new JMenuItem("Embaralhar Tabuleiro");
		mntmEmbaralharTabuleiro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				embaralharTabuleiro();
			}
		});
		mnMenu.add(mntmEmbaralharTabuleiro);

		JMenuItem mntmBuscarSoluo = new JMenuItem("Buscar Solu\u00E7\u00E3o");
		mntmBuscarSoluo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IniciarBusca();
			}
		});
		mnMenu.add(mntmBuscarSoluo);

		btn1 = new JButton("1");
		btn1.setBackground(Color.LIGHT_GRAY);
		btn1.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn1.setBounds(74, 105, 158, 120);
		frmPuzzle.getContentPane().add(btn1);

		btn2 = new JButton("2");
		btn2.setBackground(Color.LIGHT_GRAY);
		btn2.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn2.setBounds(232, 105, 158, 120);
		frmPuzzle.getContentPane().add(btn2);

		btn3 = new JButton("3");
		btn3.setBackground(Color.LIGHT_GRAY);
		btn3.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn3.setBounds(391, 105, 158, 120);
		frmPuzzle.getContentPane().add(btn3);

		btn4 = new JButton("4");
		btn4.setBackground(Color.LIGHT_GRAY);
		btn4.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn4.setBounds(74, 225, 158, 120);
		frmPuzzle.getContentPane().add(btn4);

		btn5 = new JButton("5");
		btn5.setBackground(Color.LIGHT_GRAY);
		btn5.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn5.setBounds(232, 225, 158, 120);
		frmPuzzle.getContentPane().add(btn5);

		btn6 = new JButton("6");
		btn6.setBackground(Color.LIGHT_GRAY);
		btn6.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn6.setBounds(391, 225, 158, 120);
		frmPuzzle.getContentPane().add(btn6);

		btn7 = new JButton("7");
		btn7.setBackground(Color.LIGHT_GRAY);
		btn7.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn7.setBounds(74, 345, 158, 120);
		frmPuzzle.getContentPane().add(btn7);

		btn8 = new JButton("8");
		btn8.setBackground(Color.LIGHT_GRAY);
		btn8.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn8.setBounds(232, 345, 158, 120);
		frmPuzzle.getContentPane().add(btn8);

		btn9 = new JButton("");
		btn9.setBackground(Color.LIGHT_GRAY);
		btn9.setFont(new Font("Tahoma", Font.PLAIN, 35));
		btn9.setBounds(391, 345, 158, 120);
		frmPuzzle.getContentPane().add(btn9);

	}	
	
	//retorno o valor que estar no tabuleiro
	public int getValorTabuleiro(String valor){
		//verifico se o valor passado é uma string vazia, se for ele representa o zero
		if(valor==""){
			return 0;
		}
		int retorno = Integer.parseInt(valor);
		return retorno;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void IniciarBusca(){
		//vou recuperar os valores de cada elemento do tabuleiro que estar na view
		int[] estadoatual = new int[] {getValorTabuleiro(btn1.getText()),getValorTabuleiro(btn2.getText()),getValorTabuleiro(btn3.getText())
				,getValorTabuleiro(btn4.getText()),getValorTabuleiro(btn5.getText()),getValorTabuleiro(btn6.getText())
				,getValorTabuleiro(btn7.getText()),getValorTabuleiro(btn8.getText()),getValorTabuleiro(btn9.getText())};

		fila = new PriorityQueue(5, new ComparadorNo());//inicializando a fila de prioridade
		processados = new HashSet();//inicializando que vai conter os estados processados.

		long l = System.currentTimeMillis();
		No no = iniciarBusca(estadoatual);//vou iniciar a busca da solução, utilizando o algoritimo A*.
		if (no != null) {//se o no resultante for diferente de null é porque ele encontrou uma solução pra esse estado atual.
//			imprimirResultado(no, true);
			//Vou inicar a Thread responsavel por mover os elementos, de acordo com cada estado da solução encontrada, formando o caminho ate a solução do problema.
			new Thread(new ThreadResolverTabuleiro(no)).start();
		} else {
			JOptionPane.showMessageDialog(null, "SOLUÇÃO NAO ENCONTRADA!");//Mensagem para o usuario de que a solução não foi encontrada
//			System.out.println("SOLUÇÃO NAO ENCONTRADA!");
		}
		System.out.println("\r\n\r\nTempo de execução: " + (System.currentTimeMillis() - l) + "ms");

	}
	
	public void embaralharTabuleiro(){
		//Vou inicar a Thread responsavel por embaralhar os elementos(valores) no tabuleiro.
		new Thread(new ThreadEmbaralharTabuleiro()).start();
	}
		
	public String getValorIndexTabuleiro(int valor){
		if(valor==0){
			return "";
		}
		return ""+valor;
	}

	@SuppressWarnings("unchecked")
	private static No iniciarBusca(int[] posicoesIniciais) {
		No noInicial = new No();//Crio o no inicial
		
		noInicial.estado = posicoesIniciais;//atribuo ao meu estado inicial as posições atual em que se encontra o tabuleiro.
		
		fila.add(noInicial);//adiciono a fila o meu no inicial
		tamanho_fila = 1;//seto o tamanho da fila igual a 1
		
		while (!(fila.isEmpty())) {//enquanto existir elementos(estados) para ser explorados, continuo explorando os estados procurando a solução.
			tamanho_fila--;
			No no = (No) fila.remove();// removo o no de maior prioridade que estar na fila, e atribuo a variavel
			
			if (goal(no.estado)) {//Verifico se o meu estado atual é igual a salução
				custo_solucao = no.custoCaminho;//pego o custo do caminho pra encontrar a solução
				return no;//retorno o no 
			}
			adicionarNosAlternativosFila(no);//Vou expandir os nos de acordo com o meu estado atual a fim de encontrar a solução
			tamanho_fila = fila.size();//atualizo o tamanho da fila
			if (tamanho_fila_max < tamanho_fila) tamanho_fila_max = tamanho_fila; // estatistica
		}
		return null; //se chegou aqui é porque falhou, ou seja ja a fila esta vazia, todos os estados possiveis ja foram explorados e não encontrei a saolução.
	}

	

	/**
	 * Esta é a chave do problema, determinar uma função heuristica que retorne
	 *
	 * o quanto esta perto da solucao neste problema. A heuristica é calculada
	 *
	 * verificando quantos blocos estão na posicao correta - isto é, de acordo
	 *
	 * com a matriz 'solucão'
	 */
	private static double heuristica(int[] estado) {
		double valor = 0.0;
		for (int i = 0; i < dimensao; i++) {
			for (int j = 0; j < dimensao; j++) {
				int n = i * dimensao + j;
				valor += estado[n] == solucao[n] ? 1 : 0;
			}
		}
		return valor;
	}
	

	private static boolean goal(int[] estado) {
		//compara cada valor que estar no array estado com o array da solucao para ver se estão na mesma
		//ordem da solucao, se tiver algum valor diferente da ordem retorno falso indicando que não encontrei a solução ainda.
		for (int i = 0; i < dimensao * dimensao; i++) {
			if ((estado[i] != solucao[i])) return false;
		}
		return true;
	}

	private static List<Sucessor> recuperarSucessores(int[] estado) {
		List<Sucessor> filhos = new ArrayList<Sucessor>();
		
		
		if (podeMoverPeca(estado, CIMA)) {//vou verificar se posso mover a peça para cima
			int[] novoEstado = clonar(estado);//crio uma copia do estado atual, para que eu possa realizar modificação assim criando novo estado.
			moverCima(novoEstado);//Vou mover a peça pra cima, ou seja mover o zero pra cima e o valor que esta em cima desce para a posição que o zero ocupava.
			filhos.add(new Sucessor(novoEstado, CIMA));
		}
		if (podeMoverPeca(estado, ESQUERDA)) {//vou verificar se posso mover a peça para esquerda
			int[] novoEstado = clonar(estado);//crio uma copia do estado atual, para que eu possa realizar modificação assim criando novo estado.
			moverEsquerda(novoEstado);//Vou mover a peça pra esquerda, ou seja mover o zero pra esquerda e o valor que esta na esquerda vem para a posição que o zero ocupava.
			filhos.add(new Sucessor(novoEstado, ESQUERDA));
		}
		if (podeMoverPeca(estado, DIREITA)) {//vou verificar se posso mover a peça para direita
			int[] novoEstado = clonar(estado);//crio uma copia do estado atual, para que eu possa realizar modificação assim criando novo estado.
			moverDireita(novoEstado);//Vou mover a peça pra direita, ou seja mover o zero pra direita e o valor que esta na direita vem para a posição que o zero ocupava.
			filhos.add(new Sucessor(novoEstado, DIREITA));
		}
		if (podeMoverPeca(estado, BAIXO)) {//vou verificar se posso mover a peça para baixo
			int[] novoEstado = clonar(estado);//crio uma copia do estado atual, para que eu possa realizar modificação assim criando novo estado.
			moverBaixo(novoEstado);//Vou mover a peça pra baixo, ou seja mover o zero pra baixo e o valor que esta em baixo sobe para a posição que o zero ocupava.
			filhos.add(new Sucessor(novoEstado, BAIXO));
		}
		return filhos;
	}

	private static void moverCima(int[] estado) {
		int pos = 0;
		for (int i = dimensao; i < dimensao * dimensao; i++) {//aqui inicializo a variavel i com a dimenção igual a 3, pois as possiveis posições que posso mover é a : 3,4,5,6,7,8 
			if (estado[i] == 0) {//verifico se o valor da posição i é igual a zero, então alimento a variavel pos, com a posição atual da variavel i, para que eu possa mover.
				pos = i;//atribuo o valo de i(posição atual do zero), para a variavel pos.
				break;
			}
		}
		if (pos > 0) {
			int valor = estado[pos - dimensao];//pego o valor que esta na posição que vai ser movido para a posição em que o zero esta e movo o zero para a posição em que o valor se encontra, fazendo isso crio um novo estado.
			estado[pos - dimensao] = 0;//movo o zero para a posição em que o valor se encontrava, fazendo o movimento de mover para cima.
			estado[pos] = valor;// e coloco o valor que recuperei anteriormente na posição em que o zero estava.
		}
	}

	private static void moverBaixo(int[] estado) {
		int pos = 0;
		for (int i = 0; i < dimensao * dimensao; i++) {
			if (estado[i] == 0) {//verifico se o valor da posição i é igual a zero, então alimento a variavel pos, com a posição atual da variavel i, para que eu possa mover.
				pos = i;//atribuo o valo de i(posição atual do zero), para a variavel pos.
				break;
			}
		}
		int valor = estado[pos + dimensao];//pego o valor que esta na posição que vai ser movido para a posição em que o zero esta, e movo o zero para a posição em que o valor se encontra, fazendo isso crio um novo estado.
		estado[pos + dimensao] = 0;//movo o zero para a posição em que o valor se encontrava, fazendo o movimento de mover para baixo.
		estado[pos] = valor;// e coloco o valor que recuperei anteriormente na posição em que o zero estava.
	}

	private static void moverEsquerda(int[] estado) {
		int pos = 0;
		for (int i = 0; i < dimensao * dimensao; i++) {
			if (estado[i] == 0) {//verifico se o valor da posição i é igual a zero, então alimento a variavel pos, com a posição atual da variavel i, para que eu possa mover.
				pos = i;//atribuo o valo de i(posição atual do zero), para a variavel pos.
				break;
			}
		}
		int valor = estado[pos - 1];//pego o valor que esta na posição que vai ser movido para a posição em que o zero esta, e movo o zero para a posição em que o valor se encontra, fazendo isso crio um novo estado.
		estado[pos - 1] = 0;//movo o zero para a posição em que o valor se encontrava, fazendo o movimento de mover para esquerda.
		estado[pos] = valor;// e coloco o valor que recuperei anteriormente na posição em que o zero estava.
	}

	private static void moverDireita(int[] estado) {
		int pos = 0;
		for (int i = 0; i < dimensao * dimensao; i++) {
			if (estado[i] == 0) {//verifico se o valor da posição i é igual a zero, então alimento a variavel pos, com a posição atual da variavel i, para que eu possa mover.
				pos = i;//atribuo o valo de i(posição atual do zero), para a variavel pos.
				break;
			}
		}
		int valor = estado[pos + 1];//pego o valor que esta na posição que vai ser movido para a posição em que o zero esta, e movo o zero para a posição em que o valor se encontra, fazendo isso crio um novo estado.
		estado[pos + 1] = 0;//movo o zero para a posição em que o valor se encontrava, fazendo o movimento de mover para esquerda.
		estado[pos] = valor;// e coloco o valor que recuperei anteriormente na posição em que o zero estava.
	}

	/**
	 * Expandir e adicionar nós subsequentes na fila
	 */
	private static void adicionarNosAlternativosFila(No no) {
		//primeiro verifico se o no(estado atual) ainda não foi processado, então adiciono na lista de processados
		//para que ele não processe uma nova folha caso ele encontre o mesmo valor a medida que for percorrendo cada 
		//estado
//		String estadoatual = no.toString();
		if (!(processados.contains(no.toString()))) {
			processados.add(no.toString());//adiciono o meu estado atual a lista de processados.
			List<No> expandidos = expandirNos(no);//Vou expandir os nos apartir do meu estado atual
			for (No o : expandidos) {
				fila.add(o);//adiciono a fila de prioridade
			}
		}
	}

	/**
	 * Retorna um estado clonado
	 *
	 */
	private static int[] clonar(int[] estado) {
		//retorna um estado clonado
		int[] ret = new int[estado.length];
		for (int i = 0; i < estado.length; i++) {
			ret[i] = estado[i];
		}
		return ret;
	}

	/**
	 * Expandir o nó com os seus possíveis sucessores
	 */
	private static List<No> expandirNos(No no) {
		List<No> nos = new ArrayList<No>();
		List<Sucessor> proximos = recuperarSucessores(no.estado);//Vou recuperar os nos sucessores possiveis de acordo com o meu estado atual
		for (Sucessor prox : proximos) {
			No no0 = new No();//crio um novo no
			no0.pai = no;//aponto para o pai
			no0.estado = prox.estado;//seto o estado atual que é dos estados sucessor
			no0.step = no.step + 1;//seto o step
			no0.acao = prox.acao;//informo qual a ação
			no0.custoStep = 1.0; // o custo é sempre 1 pois é movido 1 bloco a cada passo
			no0.custoCaminho += no0.pai.custoCaminho + 1.0;//atualizo a heuristica somando com o custo do caminho a
			nos.add(no0);//adicionando a lista de nos
		}
		nos_expandidos++;//atualizo a variavel com mais 1;
		return nos;
	}

	/**
	 * Verifica se a peca pode ser movido para uma direção(ação)
	 */
	private static boolean podeMoverPeca(int[] estado, int acao) {
		int posicao = 0;
		for (int i = 0; i < dimensao * dimensao; i++) {
			if (estado[i] == 0) {
				posicao = i;
				break;
			}
		}
		if (acao == ESQUERDA) {
			while (posicao >= 0) {
				if (posicao == 0) return false;
				posicao -= dimensao;
			}
		} else if (acao == CIMA) {
			if (posicao >= 0 && posicao < dimensao) return false;
		} else if (acao == DIREITA) {
			posicao++;
			while (posicao >= dimensao) {
				if (posicao == dimensao) return false;
				posicao -= dimensao;
			}
		} else if (acao == BAIXO) {
			if (posicao >= dimensao * (dimensao - 1)) return false;
		}
		return true;
	}

	/**
	 * Representa um estado sucessor ao estado atual dada uma ação
	 */
	static class Sucessor {
		public Sucessor(int _estado[], int _acao) {
			estado = _estado;
			acao = _acao;
		}

		public int[] estado;
		public int acao;
	}

	/**
	 * Representa um nó de um grafo. Possuindo os estados: o nó pai,
	 *
	 * a acao, o custo para chegar até este nó, a profundidade do nó
	 *
	 * e o custo unitário para ir do pai até o filho
	 */
	static class No {
		public int[] estado;
		public int acao;
		public No pai;
		public int step = 0;
		public double custoCaminho = 0.0;
		public double custoStep = 0.0;

		public List<No> recuperarArvore() {
			No atual = this;
			List<No> ret = new ArrayList<>();
			while (!(atual.pai != null)) {
				ret.add(0, atual);
				atual = atual.pai;
			}
			ret.add(0, atual);
			return ret;
		}

		@Override
		public String toString() {
			//sobrescrevendo o metodo toString para que ele retorne os valores do estado atual.
			String ret = "";
			for (int i = 0; i < dimensao * dimensao; i++) {
				ret += estado[i];
			}
			return ret;
		}

		@Override
		public boolean equals(Object o) {
			if ((o == null) || (this.getClass() != o.getClass())) return false;
			if (this == o) return true;
			No x = (No) o;
			for (int i = 0; i < dimensao; i++) {
				if (estado[i] != x.estado[i]) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * comparador de Nós chamando a heuristica, quem chama essa classe é PriorityQueue, para eleger o estado com maior prioridade.
	 *
	 */
	static class ComparadorNo implements Comparator {

		@Override
		public int compare(Object arg0, Object arg1) {
			No o1 = (No) arg0;
			No o2 = (No) arg1;
			double d1 = heuristica(o1.estado);
			double d2 = heuristica(o2.estado);
			return (int) (d2 - d1);
		}
	}

	private class ThreadResolverTabuleiro implements Runnable {
		private No no;
		
		public ThreadResolverTabuleiro(No no) {
			// TODO Auto-generated constructor stub
			this.no = no;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int[] estadoFinal = null;
			LinkedList<int[]> b = new LinkedList<>();
			while (no.pai != null) {

				estadoFinal = no.estado;
				b.addFirst(estadoFinal);
				no = no.pai;
			}


			for (int[] is : b) {
				btn1.setText(getValorIndexTabuleiro(is[0]));
				btn2.setText(getValorIndexTabuleiro(is[1]));
				btn3.setText(getValorIndexTabuleiro(is[2]));
				btn4.setText(getValorIndexTabuleiro(is[3]));
				btn5.setText(getValorIndexTabuleiro(is[4]));
				btn6.setText(getValorIndexTabuleiro(is[5]));
				btn7.setText(getValorIndexTabuleiro(is[6]));
				btn8.setText(getValorIndexTabuleiro(is[7]));
				btn9.setText(getValorIndexTabuleiro(is[8]));
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private class ThreadEmbaralharTabuleiro implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Random random = new Random();
			int[] estadoFinal = {1,2,3,4,5,6,7,8,0};

			for(int i=0;i<30;i++){
				for (int ii=0; ii < (estadoFinal.length - 1); ii++) {

					//sorteia um índice
					int j = random.nextInt(estadoFinal.length); 

					//troca o conteúdo dos índices i e j do vetor
					int temp = estadoFinal[ii];
					estadoFinal[ii] = estadoFinal[j];
					estadoFinal[j] = temp;
				}
				btn1.setText(getValorIndexTabuleiro(estadoFinal[0]));
				btn2.setText(getValorIndexTabuleiro(estadoFinal[1]));
				btn3.setText(getValorIndexTabuleiro(estadoFinal[2]));
				btn4.setText(getValorIndexTabuleiro(estadoFinal[3]));
				btn5.setText(getValorIndexTabuleiro(estadoFinal[4]));
				btn6.setText(getValorIndexTabuleiro(estadoFinal[5]));
				btn7.setText(getValorIndexTabuleiro(estadoFinal[6]));
				btn8.setText(getValorIndexTabuleiro(estadoFinal[7]));
				btn9.setText(getValorIndexTabuleiro(estadoFinal[8]));

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Inicializando a aplicação
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JogoDosOito window = new JogoDosOito();
					window.frmPuzzle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	

}

