package HotelDesafioDeRebeca;

import java.util.*;
import java.util.concurrent.*;

public class SimulacaoReservasHotel {
	
	public static void main(String[] args) {
		
        int quantidadeQuartosIniciais = 5;
        Hotel hotel = new Hotel(quantidadeQuartosIniciais);

        // ExecutorService: piscina de threads(trabalhadores) + fila de tarefas interna.
        ExecutorService executor = Executors.newFixedThreadPool(8);

        // Sincronização: esperar TODAS as tarefas terminarem.
        CountDownLatch barreiraFinalizacao = new CountDownLatch(10);

        List<String> usuarios = Arrays.asList(
            "João", "Maria", "Pedro", "Ana", "Carlos",
            "Julia", "Lucas", "Beatriz", "Fernando", "Sofia"
        );

        System.out.println("=== INICIANDO RESERVAS(CONCORRENTES) ===\n");

        for (String usuario : usuarios) {
            executor.submit(() -> {
                hotel.tentarReservar(usuario);
                barreiraFinalizacao.countDown();
            });
        }

        // Espera sincronizada por todas as threads ou trabalhadores.
        try {
            barreiraFinalizacao.await();
            System.out.println("\n✅ Todas as tentativas de reserva foram processadas!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();

        hotel.mostrarStatusFinal();
    }
}