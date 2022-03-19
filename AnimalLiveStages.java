public enum AnimalLiveStages
{
    GROWTH(1),  REPRODUCTION(3), DYING(1);

    private int circles;

    AnimalLiveStages(int c){ circles = c; }

    public int getCircles() {
        return circles;
    }

}
