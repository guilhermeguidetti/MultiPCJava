public class Vector2 <X> implements Cloneable
{
    private static final int TAMANHO_INICIAL = 10;

    private Object[] elem; //private X[] elem;
    private int qtd;

    public Vector2 ()
    {
      //this.elem = new X [Vector.TAMANHO_INICIAL]; <- nao compila
        this.elem = new Object [Vector2.TAMANHO_INICIAL];
        this.qtd  = 0;
    }

    public Vector2 (int size)
    {
      //this.elem = new X [Vector.TAMANHO_INICIAL]; <- nao compila
        this.elem = new Object [size];
        this.qtd  = 0;
    }

    public int getOccupied ()
    {
        return this.qtd;
    }

    public int getTotalSize() {
        return this.elem.length;
    }

    private void redimensioneSe (float taxaDeRedim)
    {
        Object[] novo;
        
      //novo = new X [Math.round(this.elem.length*taxaDeRedim)]; <- nao compila
        novo = new Object [Math.round(this.elem.length*taxaDeRedim)];

        for (int i=0; i<this.qtd; i++)
            novo[i] = this.elem[i];

        this.elem = novo;
    }

    public Vector2<X> slice(int inicial, int p) {
        Vector2<X> newVector = new Vector2<X>();
        for (int i = inicial; i < p; i++) {
            newVector.add((X)this.elem[i]); // Tenho certeza que o tipo do elemento será igual
        }
        return newVector;
    }

    public void add (X x)
    {
        if (this.qtd==this.elem.length)
            this.redimensioneSe (2.0F);

        this.elem[this.qtd] = x;
        this.qtd++;
    }

    public X get (int posicao) // posicao vai de 0 a this.qtd-1
    {
        if (posicao<0 || posicao>this.qtd-1)
            throw new java.lang.ArrayIndexOutOfBoundsException (posicao);
            
        return (X)this.elem[posicao];
    }

    public void remove (int posicao) throws ArrayIndexOutOfBoundsException
    {
        if (posicao<0 || posicao>this.qtd-1)
            throw new java.lang.ArrayIndexOutOfBoundsException (posicao);

        for (int i=posicao+1; i<this.qtd; i++)
            this.elem[i-1] = this.elem[i];

        this.qtd--;
        this.elem[this.qtd] = null;

        if (this.elem.length>Vector2.TAMANHO_INICIAL &&
            this.qtd<=Math.round(this.elem.length*0.25F))
            this.redimensioneSe (0.5F);
    }

    public boolean contains(X obj) {
        if (obj.getClass() != this.getClass())
            return false;

        for (int i = 0; i < this.elem.length; i++) {
            if (this.elem[i].equals(obj))
                return true;
        }
        return false;
    }

    @Override
    public String toString ()
    {
        String ret="[";

        for (int i=0; i<this.qtd-1; i++)
            ret = ret+this.elem[i]+", ";

        if (this.qtd>0)
            ret = ret+this.elem[this.qtd-1];

        return ret+"]";
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this==obj)
            return true;
            
        if (obj==null)
            return false;
            
        if (this.getClass()!=obj.getClass())
            return false;

        Vector2<X> vec = (Vector2<X>)obj;

        for (int i=0; i<this.qtd; i++)
            if (!this.elem[i].equals(vec.elem[i]))
                return false;
                
        return true;
    }

    @Override
    public int hashCode ()
    {
        int ret=1;

        ret = 13*ret + Integer.valueOf(this.qtd).hashCode();

        for (int i=0; i<this.qtd; i++)
            ret = 13*ret + this.elem[i].hashCode();

        if (ret<0) ret=-ret;

        return ret;
    }

    public Vector2 (Vector2<X> modelo) throws Exception
    {
        if (modelo==null) throw new Exception ("modelo ausente");

        this.qtd=modelo.qtd;

        // this.elem = new X[modelo.elem.length];
        this.elem = new Object [modelo.elem.length];

        for (int i=0; i<this.qtd; i++)
            this.elem[i]=modelo.elem[i];
    }

    @Override
    public Object clone ()
    {
        Vector2<X> ret=null;

        try
        {
            ret = new Vector2<X> (this);
        }
        catch (Exception erro)
        {} // ignoro, pq sei que no try nao ocorrera excecoes

        return ret;
    }
}
