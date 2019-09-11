package az.siftoshka.habitube.utils.navigation;

import java.util.HashMap;
import java.util.Map;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Router;

public class LocalRouter {

    private Map<String, Cicerone<Router>> containers;

    public LocalRouter() {
        containers = new HashMap<>();
    }

    public Cicerone<Router> getCicerone(String containerTag) {
        if (!containers.containsKey(containerTag)) {
            containers.put(containerTag, Cicerone.create());
        }
        return containers.get(containerTag);
    }

}
