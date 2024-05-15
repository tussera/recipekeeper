create table if not exists public.recipe(
    id serial primary key,
    title varchar(255) not null,
    number_of_servings integer not null,
    is_vegetarian boolean default 't' not null,
    instructions text not null
);

create table if not exists public.ingredient(
    id serial primary key,
    name varchar(255) not null,
    recipe_id integer not null,
    CONSTRAINT fk_recipe
        FOREIGN KEY(recipe_id)
        REFERENCES Recipe(id)
        ON DELETE CASCADE
);

create sequence if not exists public.seq_recipe_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

create sequence if not exists public.seq_ingredient_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;