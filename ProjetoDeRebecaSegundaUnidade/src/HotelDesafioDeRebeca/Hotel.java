package HotelDesafioDeRebeca;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Hotel {
	
	    private int quartosDisponiveis;
	    private final ReentrantLock travaAcesso = new ReentrantLock();           // Controle fino de acesso.
	    private final Semaphore limiteQuartos;                                  // Controle de recursos (limite de quartos).
	    private final BlockingQueue<String> filaDeEspera = new LinkedBlockingQueue<>(); // Fila de espera.

	    public Hotel(int quantidadeInicial) {
	        this.quartosDisponiveis = quantidadeInicial;
	        this.limiteQuartos = new Semaphore(quantidadeInicial, true);
	    }

	    public void tentarReservar(String usuario) {
	        System.out.println("🔄 " + usuario + " tentando reservar um quarto...");

	        // Tenta adquirir um quarto (sincronização com Semaphore).
	        if (limiteQuartos.tryAcquire()) {
	            travaAcesso.lock(); // Proteção extra no contador compartilhado.
	            try {
	                quartosDisponiveis--;
	                System.out.println("✅ " + usuario + " reservou com sucesso! Quartos restantes: " + quartosDisponiveis);

	                // Simula tempo de check-in / uso do quarto.
	                Thread.sleep(200);

	                // Libera o quarto → permite que outros da fila tenham chance.
	                limiteQuartos.release();
	                quartosDisponiveis++;
	                System.out.println("🔓 " + usuario + " liberou o quarto.");
	            } catch(InterruptedException e) {
	                Thread.currentThread().interrupt();
	            } finally {
	                travaAcesso.unlock();
	            }
	        } else{
	            // Não conseguiu → entra na fila de espera.
	            filaDeEspera.offer(usuario);
	            System.out.println("⏳ " + usuario + " não conseguiu e entrou na fila de espera.");
	        }
	    }

	    public void mostrarStatusFinal() {
	        System.out.println("\n=== STATUS FINAL DO HOTEL ===");
	        System.out.println("Quartos disponíveis: " + quartosDisponiveis);
	        System.out.println("Pessoas na fila de espera: " + filaDeEspera.size());
	        if (!filaDeEspera.isEmpty()) {
	            System.out.println("Fila de espera: " + filaDeEspera);
	        }
	    }
	}
