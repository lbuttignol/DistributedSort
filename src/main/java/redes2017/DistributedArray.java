package redes2017;

import java.lang.IndexOutOfBoundsException;
import java.util.Arrays;
import java.util.Collections;

/**
 *  This class represents a node on the distributed array
 */
public class DistributedArray{
    
    /**
     *  Total length of the distributed array, has the number of elements that
     *  the Distributed array could contains.
     */
    private Integer totalLength;

    /**
     *  Local length, local array size on this node of the distributed array 
     */
    private Integer localLength;

    /**
     *  list store the elements of this distributed array node  
     */
    private int[] list;

    /**
     *  Number of nodes that build this distributed array  
     */
    private Integer partitions;

    /**
     *  Node number, process id or partition number. Has the node number on this
     *  system, start on 0 until this.partitions - 1
     */
    private Integer procId;

    /**
     *  Elements added to the last partition. This are used to resolve the 
     *  problem of this.totalLength % this.partitions != 0. These elements are
     *  added on the last partition of the distributed array.
     */
    private Integer resto;

    /**
     *  Internal name of the array, this should be unique. Used to bind it with
     *  the middlewar. 
     */
    private String name;

    /**
     *  Counter of DistributedArray instances. This is used to create the name 
     *  of the instance. 
     */
    private static Integer counter = 0;

    /**
     *  Message manager 
     */
    private Middlewar secretary;

    /**
     *  Distributed Array constructor
     *  @param size of the array
     *  @param mid the middlewar that assist to this instance of distributed array
     */
    public DistributedArray(Integer size, Middlewar mid ){
        this.secretary   = mid;
        this.procId      = this.secretary.whoAmI();
        this.totalLength = size;

        //name treatment
        this.name = "a" + this.counter.toString();
        this.counter++;

        // bind this array on the middlewar
        this.secretary.bind(this.name,this);
        
        DistSystem sys   = this.secretary.getSys();
        this.partitions  = sys.size();
        this.resto       = size % partitions;
        this.localLength = size / partitions; 
        
        if(resto != 0 && this.secretary.iAmLast()){
            this.localLength += this.resto;
        }
        this.list = new int[localLength];

    }

    /**
     *  @param index on the Distributed Array
     *  @return True iff the given index is on this node of the array
     */
    private boolean isHere(Integer index){
        return index >= this.lowerIndex(this.procId) && index <= this.upperIndex(this.procId);
    }

    /**
     *  @param index on the Distributed Array
     *  @return True iff the given index belongs to the array. 
     */
    private boolean rightIndex(Integer index){
        return index >= 0 && index < this.totalLength;
    }

    /**
     *  @param index on the Distributed Array
     *  @preturn the process number that has the given index
     */
    private Integer whoGotIt(Integer index){
        if(!this.rightIndex(index)){
            throw new IndexOutOfBoundsException("Index out of bound on whoGotIt");
        }
        Integer result = index / (this.totalLength / this.partitions);

        if (result > this.partitions - 1) {
            result = this.partitions - 1;
        }
        return result;
    }

    /**
     *  Change the value on the given index with val
     *  @param index to set on the array 
     *  @param val value to put on the index 
     */
    public void set(Integer index, Integer val){
        if (!this.rightIndex(index)) {
            throw new IndexOutOfBoundsException("Index out of bound on set");
        }
        if(!this.isHere(index)) {
            synchronized (this){
                this.secretary.sendTo(this.whoGotIt(index), MessageType.SET.toString() + " " + this.name + " " + index + " " + val + " ");
            }
        }else {
            this.list[index - this.lowerIndex(this.procId)] = val;
        }
    }

    /**
     *  @param index Integer that represent a position on the array.
     *  @return the value on the given index.
     */
    public Integer get(Integer index){
        if (!this.rightIndex(index)) {
            throw new IndexOutOfBoundsException("Index out of bound on get");
        }

        if(!this.isHere(index)){
            synchronized(this){
                this.secretary.sendTo(this.whoGotIt(index) , MessageType.GET.toString() +" " + this.name + " " + index.toString() + " " + this.procId + " " );
                String message = this.secretary.receiveFrom(this.whoGotIt(index), MessageType.GETR);
                return Message.getIntParam(message,3);
            }   
        }
        return this.list[index - this.lowerIndex(this.procId)];
    }

    /**
     *  @param procId Integer that represents a distributed array node 
     *  @return the first global index that has the node procId
     */ 
    private Integer lowerIndex(Integer procId){
        return procId * this.totalLength / this.partitions;
    }

    /**
     *  @param procId Integer that represents a distributed array node 
     *  @return the last global index that has the node procId
     */
    private Integer upperIndex(Integer procId){ 
        return procId * this.totalLength / this.partitions + this.localLength - 1;
    }

    /**
     *  Sort the array memory available on the distributed array node
     */
    private void internalSort(){
        Arrays.sort(this.list);
    }

    /**
     *  Swap two elements on the Array
     *  @param e0 global index to swap
     *  @param e1 global index to swap
     */
    private void swap(Integer e0,Integer e1) {
            Integer aux0 = this.get(e0);
            Integer aux1 = this.get(e1);
            this.set(e1, aux0);
            this.set(e0, aux1);

    }   

    /**
     *  Sort the distributed array from the smaller to the higher
     */
    public void distributedSort() {
        this.secretary.barrier();
        boolean finish = false;
        while (! finish) {
            finish = true;
            this.internalSort();
            this.secretary.barrier();
            if (this.procId != this.partitions-1) {
                Integer aux0,aux1,aux2,aux3;
                aux0=this.upperIndex(this.procId);
                aux1=this.lowerIndex(this.procId+1);
                aux2=this.get(aux0);
                aux3=this.get(aux1);
                if (this.get(this.upperIndex(this.procId)).compareTo(this.get(this.lowerIndex(this.procId + 1))) > 0) {
                    this.swap(this.upperIndex(this.procId),this.lowerIndex(this.procId + 1));
                    finish = false;
                }
            }

            finish = secretary.andReduce(finish);
        }

    }

    /**
     *  Array node toString
     */
    @Override
    public String toString(){
        return  "Array Name "    + this.name + "\n" +
                "Global Length " + this.totalLength + "\n" +
                "Local Length "  + this.localLength + "\n" +
                "Node Number "   + this.procId + "\n" +
                "List Length "   + this.list.length + "\n" +
                "Lower Index "   + this.lowerIndex(this.procId) + "\n" +
                "Lower Index "   + this.get(this.lowerIndex(this.procId)) + "\n" +
                "Upper Index "   + this.upperIndex(this.procId) + "\n" +
                "Upper Index "   + this.get(this.upperIndex(this.procId)) + "\n" +
                "List = "        + this.aux() ;
    }

    private String aux(){
        String result ="";
        for (int i=0; i<this.localLength; i++) {
            result = result + list[i] + "\n";
        }
        return result;
    }
    

}

