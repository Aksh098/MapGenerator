package MapGenerator;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.util.*;

public class MapGenerator {

    private static final int NUM_CITIES = 100;
    private static final int MAX_LATITUDE = 90;
    private static final int MAX_LONGITUDE = 180;
    private static final Random random = new Random();

    public static void main(String[] args) {
        List<City> cities = generateRandomCities(NUM_CITIES);
        printCities(cities);
        List<Road> roads = generateRoadConnections(cities);
        visualizeMap(cities, roads);
    }

    private static List<City> generateRandomCities(int numCities) {
        List<City> cities = new ArrayList<>();
        Set<String> cityNames = new HashSet<>();
        for (int i = 0; i < numCities; i++) {
            String cityName;
            do {
                cityName = "City" + (i + 1);
            } while (cityNames.contains(cityName));
            cityNames.add(cityName);
            double latitude = random.nextDouble() * MAX_LATITUDE * 2 - MAX_LATITUDE;
            double longitude = random.nextDouble() * MAX_LONGITUDE * 2 - MAX_LONGITUDE;
            cities.add(new City(cityName, latitude, longitude));
        }
        return cities;
    }

    private static void printCities(List<City> cities) {
        for (City city : cities) {
            System.out.println(city);
        }
    }

    private static List<Road> generateRoadConnections(List<City> cities) {
        List<Road> roads = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            City city1 = cities.get(i);
            for (int j = i + 1; j < cities.size(); j++) {
                City city2 = cities.get(j);
                int lanes = random.nextInt(4) + 1;
                roads.add(new Road(city1, city2, lanes));
            }
        }
        return roads;
    }

    private static void visualizeMap(List<City> cities, List<Road> roads) {
        Graph graph = new SingleGraph("City Map");
        for (City city : cities) {
            Node node = graph.addNode(city.getName());
            node.setAttribute("ui.label", city.getName());
        }

        for (Road road : roads) {
            String edgeId = road.getCity1().getName() + "-" + road.getCity2().getName();
            Edge edge = graph.addEdge(edgeId, road.getCity1().getName(), road.getCity2().getName());
            edge.setAttribute("ui.label", road.getLanes() + " lanes");
            switch (road.getLanes()) {
                case 4:
                    edge.setAttribute("ui.style", "fill-color: red;");
                    break;
                case 3:
                    edge.setAttribute("ui.style", "fill-color: orange;");
                    break;
                case 2:
                    edge.setAttribute("ui.style", "fill-color: yellow;");
                    break;
                case 1:
                    edge.setAttribute("ui.style", "fill-color: green;");
                    break;
            }
        }

        graph.display();
    }
}

class Road {
    private City city1;
    private City city2;
    private int lanes;

    public Road(City city1, City city2, int lanes) {
        this.city1 = city1;
        this.city2 = city2;
        this.lanes = lanes;
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }

    public int getLanes() {
        return lanes;
    }

    @Override
    public String toString() {
        return String.format("Road{city1=%s, city2=%s, lanes=%d}", city1, city2, lanes);
    }
}

class City {
    private String name;
    private double latitude;
    private double longitude;

    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("City{name='%s', latitude=%.2f, longitude=%.2f}", name, latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

