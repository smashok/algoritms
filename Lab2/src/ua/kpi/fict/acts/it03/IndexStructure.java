package ua.kpi.fict.acts.it03;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class IndexStructure implements IndexInterface {

    public final int KEY_LENGTH = 10;
    public final int DATA_LENGTH = 90;
    private final int MAX_NOTES_IN_BLOCK_LENGTH = 8;
    private final int BLOCKS_AMOUNT_LENGTH = 4;

    private final String INDEX_FILE_NAME = "IndexFile";
    private final String BLOCK_NAME = "Block";

    private final int DEFAULT_NOTES_IN_BLOCK = 500;
    private final int DEFAULT_AMOUNT_BLOCKS = 10;

    private final String folderPath;
    public int blocksAmount;
    public int maxNotesInBlock;


    public IndexStructure(String folderPath) throws IOException
    {
        this.folderPath = folderPath;

        File indexFile = new File(folderPath+INDEX_FILE_NAME);
        if(!indexFile.exists()) //Если индексного файла не было, то он создаётся со стондартными настройками
        {
            indexFile.createNewFile();
            this.maxNotesInBlock = DEFAULT_NOTES_IN_BLOCK;
            this.blocksAmount = DEFAULT_AMOUNT_BLOCKS;
            indexListToFile(new LinkedList<>());

            for (int i = 1; i <= blocksAmount; i++) {
                File dataFile = new File(folderPath+BLOCK_NAME+i);
                if(!dataFile.exists()) //Если дата-файла не существовало - он создаётся
                {
                    dataFile.createNewFile();
                }
            }

        }else // Если индексный файл существовал, то применяются его настройки
        {
            try(FileReader reader = new FileReader(indexFile))
            {
                int size = MAX_NOTES_IN_BLOCK_LENGTH+ BLOCKS_AMOUNT_LENGTH;
                char[] buf = new char[size];
                reader.read(buf);
                StringBuilder maxNotes = new StringBuilder();
                StringBuilder countData = new StringBuilder();
                for (int i = 0; i < MAX_NOTES_IN_BLOCK_LENGTH; i++)
                {
                    maxNotes.append(buf[i]);
                }
                for (int i = MAX_NOTES_IN_BLOCK_LENGTH; i < size; i++)
                {
                    countData.append(buf[i]);
                }
                blocksAmount = Integer.parseInt(countData.toString().trim());
                maxNotesInBlock = Integer.parseInt(maxNotes.toString().trim());
            }
        }
    }

    @Override
    public void set(String key, String value) throws IOException
    {
        int blockNum = Math.abs(key.hashCode()%blocksAmount) +1;


        LinkedList<String> indexList = getIndexList();
        Iterator<String> indexListIterator= indexList.iterator();
        while(indexListIterator.hasNext())          //Проверка наличия ключа, в случае нахождения, замена существующего значения
        {
            String _key = indexListIterator.next();
            if(_key.equals(key))
            {
                int _blockNum = Integer.parseInt(indexListIterator.next());
                LinkedList<String> dataList = getDataList(_blockNum);
                ArrayList<String> dataArr = new ArrayList<>(dataList);
                int index = binarySearch(dataArr, key);
                dataList.set(index+1, value);
                dataListToFile(dataList, _blockNum);
                return;
            }
            indexListIterator.next();
        }
        indexList.add(key);
        indexList.add(blockNum+"");
        indexListToFile(indexList);

        LinkedList<String> dataList = getDataList(blockNum);
        int blockNotesCount = 0;
        int notesAmount = dataList.size()/2+1;
        Iterator<String> dataListIterator = dataList.iterator();
        boolean added = false;
        while (dataListIterator.hasNext()) //Нахождение нужного места для вставки ключа-значения
        {
            String _key = dataListIterator.next();
            if(_key.compareTo(key)>0)
            {
                dataList.add(blockNotesCount*2, key);
                dataList.add(blockNotesCount*2+1, value);
                added = true;
                break;
            }
            dataListIterator.next();
            blockNotesCount++;
        }
        if(!added)
        {
            dataList.add(key);
            dataList.add(value);
        }
        dataListToFile(dataList, blockNum);
        if(notesAmount >= maxNotesInBlock)
        {
            reindexing();
        }
    }

    @Override
    public void delete(String key) throws  IOException
    {
        LinkedList<String> indexList = getIndexList();
        Iterator<String> iterator = indexList.iterator();
        int i = 0;

        while(iterator.hasNext())
        {
            String _key = iterator.next();
            if(_key.equals(key))
            {
                int _blockNum = Integer.parseInt(iterator.next());
                LinkedList<String> dataList = getDataList(_blockNum);
                ArrayList<String> dataArr = new ArrayList<>(dataList);
                int index = binarySearch(dataArr, key);
                dataList.remove(index);
                dataList.remove(index);
                dataListToFile(dataList, _blockNum);
                indexList.remove(i*2);
                indexList.remove(i*2);
                indexListToFile(indexList);
                return;
            }
            i++;
            iterator.next();
        }
    }

    @Override
    public String get(String key) throws IOException
    {
        LinkedList<String> indexList = getIndexList();
        Iterator<String> iterator = indexList.iterator();
        while(iterator.hasNext())
        {
            String _key = iterator.next();
            if(_key.equals(key))
            {
                int _blockNum = Integer.parseInt(iterator.next());
                LinkedList<String> dataList = getDataList(_blockNum);
                ArrayList<String> dataArr = new ArrayList<>(dataList);
                int index = binarySearch(dataArr, key);
                return dataList.get(index+1);
            }
            iterator.next();
        }

        return "";
    }

    public static int binarySearch(ArrayList<String> dataArr, String key) //Бинарный поиск, возвращает индекс ключа
    {
        int begin = 0;
        int end = dataArr.size()-1;
        int mid;
        while( begin < end)
        {
            mid = (begin+end)/2;
            if(mid % 2 == 1) //из-за того, что нас интересуют только ключи
            {
                mid+=1;
            }
            int cmp = dataArr.get(mid).compareTo(key);
            if(cmp == 0)
            {
                return mid;
            }
            else if(cmp<0)
            {
                begin = mid+1;
            }
            else
            {
                end = mid-1;
            }
        }
        return -1;
    }

    private void reindexing() throws IOException
    {
        LinkedList<String> newIndexList = new LinkedList<>();
        for (int i = blocksAmount+1; i <= blocksAmount*2 ; i++)
        {
            File dataFile = new File(folderPath+BLOCK_NAME+i);
            if(!dataFile.exists()) //Если дата-файла не существовало - он создаётся
            {
                dataFile.createNewFile();
            }

            LinkedList<String> sourceList = getDataList(i-blocksAmount);
            LinkedList<String> destList = new LinkedList<>();
            int count = sourceList.size()/4;
            for (int j = 0; j < count; j++) // Добавление второй половины элементов в новый блок и новый индексный файл
            {
                String tempKey = sourceList.removeFirst();
                String tempData = sourceList.removeFirst();
                destList.add(tempKey);
                destList.add(tempData);
                newIndexList.add(tempKey);
                newIndexList.add(i+"");
            }
            dataListToFile(destList, i);

            Iterator<String> iterator = sourceList.iterator();
            while(iterator.hasNext()) // добавление первой половины элементов в новый индексный файл
            {
                newIndexList.add(iterator.next());
                newIndexList.add((i-blocksAmount)+"");
                iterator.next();
            }
            dataListToFile(sourceList, (i-blocksAmount));
        }
        indexListToFile(newIndexList);
        this.blocksAmount *=2;
        this.maxNotesInBlock *=2;
    }


    public LinkedList<String> getIndexList() throws IOException //Переводит индексный файл в список
    {
        LinkedList<String> list = new LinkedList<>();
        try(FileReader reader = new FileReader(folderPath+INDEX_FILE_NAME))
        {
            reader.skip(MAX_NOTES_IN_BLOCK_LENGTH+ BLOCKS_AMOUNT_LENGTH);
            int size = KEY_LENGTH+ BLOCKS_AMOUNT_LENGTH;
            char[] buf = new char[size];
            while (reader.read(buf) > 0)
            {
                StringBuilder key = new StringBuilder();
                StringBuilder blockNum = new StringBuilder();
                for (int i = 0; i < KEY_LENGTH; i++)
                {
                    key.append(buf[i]);
                }
                for (int i = KEY_LENGTH; i < size; i++)
                {
                    blockNum.append(buf[i]);

                }
                list.add(key.toString().trim());
                list.add(blockNum.toString().trim());
            }
        }
        return list;
    }

    public LinkedList<String> getDataList(int blockNum) throws IOException // Переводит дата-файл в список
    {
        LinkedList<String> list = new LinkedList<>();
        try(FileReader reader = new FileReader(folderPath+BLOCK_NAME+blockNum))
        {
            int size = KEY_LENGTH+DATA_LENGTH;
            char[] buf = new char[size];

            while (reader.read(buf) > 0)
            {
                StringBuilder key = new StringBuilder();
                StringBuilder data = new StringBuilder();
                for (int i = 0; i < KEY_LENGTH; i++)
                {
                    key.append(buf[i]);
                }
                for (int i = KEY_LENGTH; i < size; i++)
                {
                    data.append(buf[i]);
                }
                list.add(key.toString().trim());
                list.add(data.toString().trim());
            }
        }
        return list;
    }

    private void indexListToFile(LinkedList<String> list) throws IOException // Переводит список в индексный файл
    {
        FileWriter writer1 = new FileWriter(folderPath+INDEX_FILE_NAME);
        writer1.write("");
        writer1.close();

        try(FileWriter writer = new FileWriter(folderPath+INDEX_FILE_NAME, true))
        {
            String maxNotes = this.maxNotesInBlock+"";
            String amountBlocks = this.blocksAmount +"";
            int maxNotesSpaces = MAX_NOTES_IN_BLOCK_LENGTH - maxNotes.length();
            int amountBlocksSpaces = BLOCKS_AMOUNT_LENGTH - amountBlocks.length();
            maxNotes += " ".repeat(maxNotesSpaces);
            amountBlocks +=" ".repeat(amountBlocksSpaces);
            writer.write(maxNotes);
            writer.write(amountBlocks);

            int size = list.size()/2;
            for (int i = 0; i < size; i++)
            {
                String key = list.removeFirst();
                String blockNum = list.removeFirst();
                int keySpaces = KEY_LENGTH - key.length();
                int blockNumSpaces = BLOCKS_AMOUNT_LENGTH - blockNum.length();
                key += " ".repeat(keySpaces);
                blockNum += " ".repeat(blockNumSpaces);
                writer.write(key);
                writer.write(blockNum);

            }
            writer.flush();
        }
    }

    private void dataListToFile(LinkedList<String> list, int blockNum) throws IOException // Переводит список в дата-файл
    {
        FileWriter writer1 = new FileWriter(folderPath+BLOCK_NAME+blockNum);
        writer1.write("");
        writer1.close();

        try(FileWriter writer = new FileWriter(folderPath+BLOCK_NAME+blockNum,true))
        {
            int size = list.size()/2;
            for (int i = 0; i < size; i++)
            {
                String key = list.removeFirst();
                String data = list.removeFirst();
                int keySpaces = KEY_LENGTH - key.length();
                int blockNumSpaces = DATA_LENGTH - data.length();
                key += " ".repeat(keySpaces);
                data += " ".repeat(blockNumSpaces);
                writer.write(key);
                writer.write(data);

            }
            writer.flush();
        }
    }
}