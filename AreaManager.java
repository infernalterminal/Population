import java.util.ArrayList;

class AreaManager
{
    private Area[][] areas;
    private int width, height;

    AreaManager(int horQuantity, int verQuantity, int sp)
    {
        areas = new Area[verQuantity][horQuantity];
        width = horQuantity;
        height = verQuantity;

        for(int i = 0; i < verQuantity; i++)
        {
            for(int j = 0; j < horQuantity; j++)
            {
                Area area = new Area(j , i , 8, 8);
                area.setName("Area" + i + "-" + j);
                area.setAreaManager(this);
                areas[i][j] = area;
                area.createFood(2);
            }
        }
        areas[0][0].createPopulation(1);
        areas[0][0].findNeighbors();
    }

    public Area[][] getAreas() {
        return areas;
    }

    public Area[] getNeighbors(int x, int y)
    {
        if(x >= width | x < 0 | y >= height | y < 0)
            return null;

        ArrayList<Area> areaArrayList = new ArrayList<Area>(8);

        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                if(j == 0 & i == 0){}
                else
                {
                    if ( (x + j >= 0) & (x + j < width) & (y + i < height) & (y + i >= 0) )
                        areaArrayList.add(areas[y + i][x + j]);
                }
            }
        }

        return areaArrayList.toArray(new Area[areaArrayList.size()]);
    }
}
