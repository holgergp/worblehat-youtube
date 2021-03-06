Meta:
@themes Book

Narrative:
In order to add new books to the library
As a librarian
I want to add books through the website

Scenario:

Given an empty library
When a librarian adds a book with title <title>, author <author>, edition <edition>, year <year>, description <description> and isbn <isbn>
Then the booklist contains a book with values title <title>, author <author>, year <year>, edition <edition>, isbn <isbn> and description <description>

Examples:

| isbn       | author           | title     | edition   | year  | description      |
| 0552131075 | Terry Pratchett  | Sourcery  | 1         | 1989  |                  |
| 0552131075 | Terry Pratchett  | Sourcery  | 1         | 1989  | A description    |

Scenario: Different books must have different properties (ISBN, title, author, edition)

Given a library, containing only one book with isbn <isbn>
When a librarian tries to add a similar book with different <title>, <author> and <edition>
Then the new book CAN NOT be added

Examples:

| isbn       | author           | edition  | title    |
| 0552131075 | Jerry Pratchett  | 1        | Sourcery |
| 0552131075 | Terry Pratchett  | 1        | Mastery  |
| 0552131075 | Terry Pratchett  | 2        | Sourcery |

Scenario: Multiple copies of the same book must share common properties (ISBN, title, author, edition)

Given a library, containing only one book with isbn <isbn>
When a librarian tries to add a similar book with same title, author and edition
Then the new book CAN be added

Examples:

| isbn       | author           | edition  | title    |
| 0552131075 | Terry Pratchett  | 1        | Sourcery |

