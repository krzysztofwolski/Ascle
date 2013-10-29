import requests
import random

f_names = []
m_names = []
l_names = []


def load_data():
    f_names = []
    m_names = []
    l_names = []
    with open('./data/imionakobiece.txt', 'r') as f:
        f_names = f.readlines()

    with open('./data/imionameskie.txt', 'r') as f:
        m_names = f.readlines()

    with open('./data/nazwiska.txt', 'r') as f:
        l_names = f.readlines()

    return f_names, m_names, l_names


def generate_user():
    sex = random.choice([True, False])
    name = ""

    if sex:
        name = random.choice(m_names).strip()
    else:
        name = random.choice(f_names).strip()

    last_name = random.choice(l_names).strip()

    birth_date = "%d-0%d-%dT%d:%d:%d.000000" %\
                 (random.randint(11, 20),
                  random.randint(2, 9),
                  random.randint(1910, 2000),
                  random.randint(11, 20),
                  random.randint(11, 50),
                  random.randint(11, 59))

    return dict(sex=sex,
                name=name,
                last_name=last_name,
                birth_date=birth_date)


f_names, m_names, l_names = load_data()

print generate_user()