class AreaManager
{
    private Area[][] areas;

    AreaManager(int horQuantity, int verQuantity, int sp)
    {
        areas = new Area[verQuantity][horQuantity];

        for(int i = 0; i < verQuantity; i++)
        {
            for(int j = 0; j < horQuantity; j++)
            {
                Area area = new Area((sp * j) , (sp * i) , sp, sp);
                area.setName("Area" + i + "-" + j);
                area.setAreaManager(this);
                areas[i][j] = area;
                area.createFood(2);
            }
        }
        areas[0][0].createPopulation(1);
    }

    public Area[][] getAreas() {
        return areas;
    }
}
