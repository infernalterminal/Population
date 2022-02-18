public class Animal extends EObject implements Runnable {

    private boolean isHungry = true;
    private boolean isFound = false;
    private float floatX, floatY;
    private int[] target;
    private String name;
    private Area area;
    private Area areaTarget;
    private Thread thread;

    public Animal(Area a, int x, int y) {
        super(x, y);
        area = a;
        floatX = 0;
        floatY = 0;
        name = "Animal";
        thread = new Thread(this, name);
        thread.start();
    }

    public void run() {
        findFood();
    }

    private int[] useSeeker(Area a, int wdt, int hgh, int x, int y) {
        Seeker fSeeker = new Seeker("Food", a.getList(), wdt, hgh, x, y);
        return fSeeker.seek();
    }

    private void findFood() {
        int horDistToFood, verDistToFood;
        int areaX = area.getX();
        int areaY = area.getY();
        int horDistToArea, verDistToArea;
        int projectionX, projectionY;
        int[] anotherTarget;

        horDistToFood = 0;
        verDistToFood = 0;

        if (area.getFoodList().length > 0) {
            target = useSeeker(area, 8, 8, posX, posY);
            areaTarget = area;
            isFound = true;
            horDistToFood = distTo(target[0], posX);
            verDistToFood = distTo(target[1], posY);
        }

        Area[] areas = area.getaNeighbors();

        for (Area a : areas) {
            horDistToArea = 0;
            verDistToArea = 0;
            projectionX = posX;
            projectionY = posY;
            int aX = a.getX();
            int aY = a.getY();

            if (aX > areaX) {
                horDistToArea = (aX - areaX - 1) * 8 + (8 - posX);
                projectionX = 0;
            } else if (aX < areaX) {
                horDistToArea = (areaX - aX - 1) * 8 + (posX + 1);
                projectionX = 7;
            }

            if (aY > areaY) {
                verDistToArea = (aY - areaY - 1) * 8 + (8 - posY);
                projectionY = 0;
            } else if (aY < areaY) {
                verDistToArea = (areaY - aY - 1) * 8 + (posY + 1);
                projectionY = 7;
            }

            int distToFood = horDistToFood + verDistToFood;
            int distToArea = horDistToArea + verDistToArea;

            if ((distToArea < distToFood) & (a.getFoodList().length > 0) & isFound) {
                anotherTarget = useSeeker(a, Math.min(distToFood - distToArea + projectionX, 8),
                        Math.min(distToFood - distToArea + projectionY, 8), projectionX, projectionY);

                if (anotherTarget[0] != -1) {
                    int horDistToAnotherFood = distTo(anotherTarget[0], projectionX);
                    int verDistToAnotherFood = distTo(anotherTarget[1], projectionY);
                    int distToAnotherFood = horDistToAnotherFood + horDistToArea + verDistToAnotherFood + verDistToArea;
                    if (distToAnotherFood < distToFood) {
                        chooseTarget(anotherTarget, a);
                        horDistToFood = horDistToAnotherFood + horDistToArea;
                        verDistToFood = verDistToAnotherFood + verDistToArea;
                    }
                }
            } else if (!isFound & a.getFoodList().length > 0) {
                int[] t = useSeeker(a, 8, 8, projectionX, projectionY);
                chooseTarget(t, a);
                int horDistToAnotherFood = distTo(target[0], projectionX);
                int verDistToAnotherFood = distTo(target[1], projectionY);
                horDistToFood = horDistToAnotherFood + horDistToArea;
                verDistToFood = verDistToAnotherFood + verDistToArea;
            }
        }
        //System.out.println("Цель: " + areaTarget.getName() + " " + target[0] + " - " + target[1]);
    }

    private int distTo(int a, int b) {
        return Math.max(a, b) - Math.min(a, b);
    }

    private void chooseTarget(int[] t, Area a) {
        target = t;
        areaTarget = a;
        isFound = true;
    }

    public Area getArea() {
        return area;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public float getFloatX() {
        return floatX;
    }

    public float getFloatY() {
        return floatY;
    }

    public void setName(String n) {
        name = n;
    }
}