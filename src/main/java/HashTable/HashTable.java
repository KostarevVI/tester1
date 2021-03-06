package HashTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class HashTable {

    private List<HashSet<Cell>> buckets;

    /**
     * Constructor of HashTable
     *
     * @param size Sets size of HashTable on creating
     */

    public HashTable(int size) {
        this.buckets = insertSets(size);
    }

    /**
     * Inserting buckets in constructor HashTable
     *
     * @param size Number of buckets to create
     * @return Returns completed List of buckets
     */

    private List<HashSet<Cell>> insertSets(int size) {
        List<HashSet<Cell>> newBucket = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            newBucket.add(new HashSet<>());
        }
        return newBucket;
    }

    /**
     * Sets bucket number to cell {@link Cell}
     *
     * @param a Receive hash code of Cell
     * @return Returns generated bucket number based on hash code of Cell{@link Cell#getKey()}
     */

    private int bucketNum(Integer a) {
        return a.hashCode() % this.buckets.size();
    }

    /**
     * Getter of HashTable.buckets
     *
     * @return Buckets of this HashTable
     */

    public List<HashSet<Cell>> getBuckets() {
        return buckets;
    }

    /**
     * Getter of HashTable.size
     *
     * @return Returns number of buckets in this HashTable
     */

    public int getTableSize() {
        return this.buckets.size();
    }

    /**
     * Number of Cells in all HashTable
     *
     * @return Returns number of Cells
     */

    public int getBucketsSize() {
        int tSize = 0;
        for (HashSet<Cell> bucket : buckets)
            for (Cell cell : bucket)
                tSize++;
        return tSize;
    }

    /**
     * Expand size of HashTable (multiplies number of buckets by 2)
     */

    private void expandSets() {
        int sizeOfBuckets = this.buckets.size();
        for (int i = 0; i < sizeOfBuckets; i++) {
            this.buckets.add(new HashSet<>());
        }
    }

    /**
     * Converting HashTable to List for Rehash method
     *
     * @return List with all Cells from HashTable
     */

    private ArrayList<Cell> tableToList() {
        ArrayList<Cell> list = new ArrayList<>();
        for (HashSet<Cell> bucket : buckets) {
            list.addAll(bucket);
        }
        return list;
    }

    /**
     * Increases the capacity of and internally reorganizes this HashTable
     */

    private void rehash() {
        ArrayList<Cell> list = tableToList();
        this.clear();
        this.expandSets();
        for (Cell cell : list) {
            for (int i = 0; i < cell.getAmount(); i++) {
                this.push(cell.getKey());
            }
        }
    }

    /**
     * Adding value in HashTable
     *
     * @param key Received value from user or else
     * @return If adding succeeds returns True, else False
     */

    public boolean push(int key) {
        int bucketNum = bucketNum(key);
        Cell cell = new Cell(key);
        if (buckets.get(bucketNum).contains(cell)) {
            //щас будет выглядеть страшно, но зато работает (надеюсь)
            buckets.get(bucketNum).stream().filter(data -> Objects.equals(cell, data)).findFirst().get().incAmount();
            return true;
        }
        int loadFactor = this.getBucketsSize() / this.getTableSize();
        if (loadFactor > this.buckets.size()) { //лучше?)
            this.rehash();
        }
        buckets.get(bucketNum).add(cell);
        return true;
    }

    /**
     * Deleting value in HashTable
     *
     * @param key Received value from user or else
     * @return If deleting succeeds returns True, else False
     */

    public boolean delete(int key) {
        if (this.find(key)) {
            int bucketNum = bucketNum(key);
            Cell targetCell = new Cell(key);
            if (buckets.get(bucketNum).contains(new Cell(key)))
                for (Cell cell : buckets.get(bucketNum))
                    if (cell.equals(targetCell)) {
                        if (cell.getAmount() > 1) {
                            cell.decAmount();
                            System.out.println("Уменьшил на 1");
                            return true;
                        }
                        buckets.get(bucketNum).remove(cell);
                        System.out.println("Удалил");
                        return true;
                    }
        }
        System.out.println("Удалять нечего");
        return false;
    }

    /**
     * Searches for value in HashTable
     *
     * @param key Received value from user or else
     * @return If the search succeeds returns True, else False
     */

    public Boolean find(int key) {
        int hash = bucketNum(key);
        return buckets.get(hash).contains(new Cell(key));
    }

    /**
     * Printing in console all Cells in HashTable (debugging info)
     */

    public void print() {
        for (int i = 0; i < buckets.size(); i++)
            for (Cell cell : buckets.get(i))
                System.out.println(i + " Key: " + cell.getKey() + " Amount:" + cell.getAmount());
        System.out.println();
    }

    /**
     * Clears HashTable
     */

    public void clear() {
        for (HashSet<Cell> bucks : buckets) {
            bucks.clear();
        }
        System.out.println("Таблица очищена");
    }

    /**
     * Checking is HashTable empty
     *
     * @return If empty returns True, else False
     */

    public Boolean isThisEmpty() {
        for (HashSet<Cell> bucks : buckets) {
            if (!bucks.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Overriding of hashCode for HashTable
     *
     * @return Generated hashCode
     */

    @Override
    public int hashCode() {
        return Objects.hash(buckets, this.buckets.size());
    }

    /**
     * Override of equals for HashTable
     *
     * @param obj Other HashTable
     * @return If HashTables equals returns True, else False
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        HashTable otherBucks = (HashTable) obj;
        if (this.getBucketsSize() != otherBucks.getBucketsSize()) {
            System.out.println("Таблицы не равны");
            return false;
        }
        for (int i = 0; i < this.buckets.size(); i++)
            if (!Objects.equals(this.getBuckets().get(i), otherBucks.getBuckets().get(i))) {
                System.out.println("Таблицы не равны");
                return false;
            }
        System.out.println("Таблицы равны");
        return true;
    }
}


