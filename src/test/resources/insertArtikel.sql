insert into artikels(naam, aankoopprijs, verkoopprijs, houdbaarheid, soort)
values('testFood', 100, 120, 5, 'F');
insert into artikels(naam, aankoopprijs, verkoopprijs, garantie, soort)
values('testNonFood', 130, 140, 2, 'NF');
insert into kortingen(artikelId, vanafAantal, percentage)
values((select id from artikels where naam = 'testFood'), 10, 5);
insert into kortingen(artikelId, vanafAantal, percentage)
values((select id from artikels where naam = 'testNonFood'), 50, 10);