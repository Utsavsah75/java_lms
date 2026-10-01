package com.library.service;

import com.library.dao.AuthorDAO;
import com.library.dao.CategoryDAO;
import com.library.model.Author;
import com.library.model.Category;
import com.library.util.BusinessException;
import com.library.util.Validator;

/** Validation + rules for categories and authors (they share the same "cannot delete while books use it" rule). */
public class CatalogService {

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();

    private static String cleanName(String raw, String label) throws BusinessException {
        String name = Validator.trim(raw).replaceAll("\\s+", " ");
        if (name.isEmpty()) {
            throw new BusinessException(label + " name is required.");
        }
        if (!Validator.maxLen(name, 100)) {
            throw new BusinessException(label + " name must be 100 characters or less.");
        }
        return name;
    }

    // ---- categories ---------------------------------------------------------------------------

    public void addCategory(String rawName, String rawStatus) throws BusinessException {
        String name = cleanName(rawName, "Category");
        if (categoryDAO.nameExists(name, 0)) {
            throw new BusinessException("This category already exists.");
        }
        categoryDAO.insert(name, "0".equals(rawStatus) ? 0 : 1);
    }

    public void updateCategory(int id, String rawName, String rawStatus) throws BusinessException {
        String name = cleanName(rawName, "Category");
        if (categoryDAO.findById(id) == null) {
            throw new BusinessException("Category not found.");
        }
        if (categoryDAO.nameExists(name, id)) {
            throw new BusinessException("Another category already uses this name.");
        }
        categoryDAO.update(id, name, "0".equals(rawStatus) ? 0 : 1);
    }

    public void deleteCategory(int id) throws BusinessException {
        Category c = categoryDAO.findById(id);
        if (c == null) {
            throw new BusinessException("Category not found.");
        }
        if (c.getBookCount() > 0) {
            throw new BusinessException("Unable to delete the category: " + c.getBookCount()
                    + " book(s) still use it. Move or delete those books first.");
        }
        categoryDAO.delete(id);
    }

    // ---- authors ------------------------------------------------------------------------------

    public void addAuthor(String rawName) throws BusinessException {
        String name = cleanName(rawName, "Author");
        if (authorDAO.nameExists(name, 0)) {
            throw new BusinessException("This author already exists.");
        }
        authorDAO.insert(name);
    }

    public void updateAuthor(int id, String rawName) throws BusinessException {
        String name = cleanName(rawName, "Author");
        if (authorDAO.findById(id) == null) {
            throw new BusinessException("Author not found.");
        }
        if (authorDAO.nameExists(name, id)) {
            throw new BusinessException("Another author already uses this name.");
        }
        authorDAO.update(id, name);
    }

    public void deleteAuthor(int id) throws BusinessException {
        Author a = authorDAO.findById(id);
        if (a == null) {
            throw new BusinessException("Author not found.");
        }
        if (a.getBookCount() > 0) {
            throw new BusinessException("Unable to delete the author: " + a.getBookCount()
                    + " book(s) still belong to this author. Move or delete those books first.");
        }
        authorDAO.delete(id);
    }
}
