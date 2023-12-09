import java.util.concurrent.Semaphore;


public class ThreadOrdena extends Thread{
    Vector2<Integer> armazenamento;
    Semaphore mutex;
    Semaphore livre;
    Semaphore ocupado;
    int valueToLookFor;
    int initialPos;
    int finalPos;
    
    public ThreadOrdena (Vector2<Integer> armz) throws Exception
    {
        if (armz==null)
            throw new Exception ("Armazenamento ausente");
            
        this.armazenamento = armz;
    }
    
    private boolean fim = false;
    private boolean achou = false;
    private int pos = -1;

    public boolean getAchou() {
        return this.achou;
    }

    private void setAchou(boolean b) {
        this.achou = b;
    }

    public int getPos() {
        return this.pos;
    }

    private void setPos(int p) {
        this.pos = p;
    }

    public void morra ()
    {
        this.fim=true;
    }

    public Vector2<Integer> ordenarR(Vector2<Integer> v) {
        if (v.getOccupied() <= 1) return v;

        Vector2<Integer> ordenado = new Vector2<>(v.getOccupied()); 

        int metade = v.getOccupied()/2;
        Vector2<Integer> e = v.slice(0, metade);
        Vector2<Integer> d = v.slice(metade, v.getOccupied());
        e = ordenarR(e); 
        d = ordenarR(d); 
       
        while(e.getOccupied() > 0 && d.getOccupied() > 0) {
            if (e.get(0) > d.get(0)) {
                ordenado.add(d.get(0));
                d.remove(0);
            } else {
                ordenado.add(e.get(0));
                e.remove(0);
            }
        }
        // os 2 vetores devem estar ordenados internamente
        while (e.getOccupied() > 0) {
            ordenado.add(e.get(0));
            e.remove(0);
        }

        while (d.getOccupied() > 0) {
            ordenado.add(d.get(0));
            d.remove(0);
        }

        return ordenado;
    }

    public Vector2<Integer> ordenar(Vector2<Integer> v1, Vector2<Integer> v2) {
        Vector2<Integer> ordenado = new Vector2<>();
        
        while(v1.getOccupied() > 0 && v2.getOccupied() > 0) {
            if (v1.get(0) > v2.get(0)) {//precisamos ordenar
                ordenado.add(v2.get(0));
                v2.remove(0);
            } else {
                ordenado.add(v1.get(0));
                v1.remove(0);
            }
        }
        // os 2 vetores devem estar ordenados internamente
        while (v1.getOccupied() > 0) {
            ordenado.add(v1.get(0));
            v1.remove(0);
        }

        while (v2.getOccupied() > 0) {
            ordenado.add(v2.get(0));
            v2.remove(0);
        }
        
        return (Vector2<Integer>) ordenado.clone();
    }

    public void run ()
    {
        if (Processador.getQtd() >= 2) {
            try {
                Vector2<Integer> chunk = armazenamento.slice(0, armazenamento.getOccupied()/2);
                ThreadOrdena threadOrdena = new ThreadOrdena(chunk);
                Processador.libera();
                threadOrdena.start();
                threadOrdena.join();

                chunk = armazenamento.slice(armazenamento.getOccupied()/2, armazenamento.getOccupied());
                ThreadOrdena threadOrdena2 = new ThreadOrdena(chunk);
                Processador.libera();
                threadOrdena2.start();
                threadOrdena2.join();

                Vector2<Integer> result1 = threadOrdena.armazenamento;
                Vector2<Integer> result2 = threadOrdena2.armazenamento;
                Vector2<Integer> ordenado = ordenar(result1, result2);

                this.armazenamento = (Vector2<Integer>) ordenado.clone();
            } catch (Exception e) {
                System.out.println("Erro");
            }
        } else {
            Vector2<Integer> ordenado = ordenarR(this.armazenamento);
            this.armazenamento = (Vector2<Integer>) ordenado.clone();
        }
    }

}
