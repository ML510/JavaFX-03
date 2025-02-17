package controller.item;

import model.Item;

import java.util.List;

public interface ItemService {
    boolean addItem (Item item);
    boolean update (Item item);
    boolean delete (Item item);
    Item searchItem (String code);
    List<Item> getAll();

}
