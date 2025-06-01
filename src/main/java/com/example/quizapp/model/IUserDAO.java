package com.example.quizapp.model;

import java.util.List;

/**
 * Interface for the Contact Data Access Object that handles
 * the CRUD operations for the Contact class with the database.
 */
public interface IUserDAO {
    /**
     * Adds a new contact to the database.
     * @param user The user to add.
     */
    public void addUser(User user);
    /**
     * Updates an existing contact in the database.
     * @param user The contact to update.
     */
    public void updateUser(User user);
    /**
     * Deletes a contact from the database.
     * @param user The contact to delete.
     */
    public void deleteUser(User user);
    /**
     * Retrieves a contact from the database.
     * @param userName The id of the contact to retrieve.
     * @return The contact with the given id, or null if not found.
     */
    public User getUser(String userName);
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts in the database.
     */
    public List<User> getAllUsers();

    public boolean checkUserPresent(String username);
}